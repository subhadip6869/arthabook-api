package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.CreateTransactionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.TransactionResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingPositionRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final HoldingPositionRepository holdingPositionRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            HoldingPositionRepository holdingPositionRepository) {
        this.transactionRepository = transactionRepository;
        this.holdingPositionRepository = holdingPositionRepository;
    }

    public Page<TransactionResponse> getTransactions(String userId, Pageable pageable) {
        return transactionRepository.findAllByUser_UserId(userId, pageable)
                .map(TransactionResponse::new);
    }

    public Page<TransactionResponse> getTransactionsByPosition(
            UUID positionId, String userId, Pageable pageable) {
        return transactionRepository.findAllByPosition_PositionIdAndUser_UserId(positionId, userId, pageable)
                .map(TransactionResponse::new);
    }

    public Page<TransactionResponse> getTransactionsByType(
            TransactionType type, String userId, Pageable pageable) {
        return transactionRepository.findAllByUser_UserIdAndTransactionType(userId, type, pageable)
                .map(TransactionResponse::new);
    }

    public Page<TransactionResponse> getTransactionsByPositionAndType(
            UUID positionId, TransactionType type, String userId, Pageable pageable) {
        return transactionRepository.findAllByPosition_PositionIdAndUser_UserIdAndTransactionType(
                positionId, userId, type, pageable
        ).map(TransactionResponse::new);
    }

    public TransactionResponse getTransaction(UUID transactionId, String userId) {
        Transaction tx = transactionRepository
                .findByTransactionIdAndUser_UserId(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
        return new TransactionResponse(tx);
    }

    @Transactional
    public TransactionResponse recordTransaction(String userId, CreateTransactionRequest request) {
        HoldingPosition position = holdingPositionRepository
                .findByPositionIdAndUser_UserId(request.positionId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding position not found with id: " + request.positionId()));

        BigDecimal fees = request.fees() != null ? request.fees() : BigDecimal.ZERO;
        BigDecimal taxes = request.taxes() != null ? request.taxes() : BigDecimal.ZERO;

        Transaction transaction = new Transaction();
        transaction.setPosition(position);
        transaction.setTransactionType(request.transactionType());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setQuantity(request.quantity());
        transaction.setUnitPrice(request.unitPrice());
        transaction.setTotalAmount(request.totalAmount());
        transaction.setFees(fees);
        transaction.setTaxes(taxes);
        transaction.setCurrency(request.currency() != null ? request.currency() : position.getHolding().getCurrency());
        transaction.setReferenceNumber(normalize(request.referenceNumber()));
        transaction.setNotes(normalize(request.notes()));

        applyPositionBalanceUpdate(position, transaction);

        Transaction savedTx = transactionRepository.save(transaction);
        return new TransactionResponse(savedTx);
    }

    // handler methods
    private void applyPositionBalanceUpdate(HoldingPosition position, Transaction tx) {
        BigDecimal currentQty = position.getQuantity() != null ? position.getQuantity() : BigDecimal.ZERO;
        BigDecimal currentCostBasis = position.getCostBasis() != null ? position.getCostBasis() : BigDecimal.ZERO;
        BigDecimal currentAvgCost = position.getAverageCost() != null ? position.getAverageCost() : BigDecimal.ZERO;

        BigDecimal txQty = tx.getQuantity() != null ? tx.getQuantity() : BigDecimal.ZERO;
        BigDecimal txTotal = tx.getTotalAmount() != null ? tx.getTotalAmount() : BigDecimal.ZERO;

        switch (tx.getTransactionType()) {
            case BUY, DEPOSIT, TRANSFER_IN -> {
                BigDecimal newQty = currentQty.add(txQty);
                // Effective investment includes brokerage/fees/taxes capitalised into cost basis
                BigDecimal additionalCost = txTotal.add(tx.getFees()).add(tx.getTaxes());
                BigDecimal newCostBasis = currentCostBasis.add(additionalCost);

                BigDecimal newAvgCost = BigDecimal.ZERO;
                if (newQty.compareTo(BigDecimal.ZERO) > 0) {
                    newAvgCost = newCostBasis.divide(newQty, 8, RoundingMode.HALF_UP);
                }

                position.setQuantity(newQty);
                position.setCostBasis(newCostBasis.setScale(4, RoundingMode.HALF_UP));
                position.setAverageCost(newAvgCost);
            }

            case SELL, WITHDRAWAL, TRANSFER_OUT -> {
                if (txQty.compareTo(currentQty) > 0) {
                    throw new IllegalArgumentException(
                            "Cannot sell or withdraw " + txQty + " units; available balance is " + currentQty
                    );
                }

                BigDecimal newQty = currentQty.subtract(txQty);
                // Reduce cost basis by sold units * average cost
                BigDecimal costOfUnitsSold = txQty.multiply(currentAvgCost);
                BigDecimal newCostBasis = currentCostBasis.subtract(costOfUnitsSold).max(BigDecimal.ZERO);

                BigDecimal newAvgCost = newQty.compareTo(BigDecimal.ZERO) == 0
                        ? BigDecimal.ZERO
                        : currentAvgCost;

                position.setQuantity(newQty);
                position.setCostBasis(newCostBasis.setScale(4, RoundingMode.HALF_UP));
                position.setAverageCost(newAvgCost);
            }

            case MATURITY -> {
                // Fixed-income instrument reached final maturity payout
                position.setQuantity(BigDecimal.ZERO);
                position.setCostBasis(BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
                position.setAverageCost(BigDecimal.ZERO.setScale(8, RoundingMode.HALF_UP));
            }

            case DIVIDEND, INTEREST, FEE, TAX, OTHER -> {
                // Yield and fee cash flows do not mutate unit quantities or unit cost basis
            }
        }
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
