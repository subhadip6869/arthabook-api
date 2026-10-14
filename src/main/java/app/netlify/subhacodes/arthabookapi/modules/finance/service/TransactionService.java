package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.CreateTransactionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.TransactionResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.UpdateTransactionRequest;
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
import java.util.Locale;
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

    @Transactional
    public TransactionResponse createTransaction(String userId, CreateTransactionRequest request) {
        HoldingPosition position = findOwnedPosition(request.positionId(), userId);

        Transaction transaction = new Transaction();

        transaction.setPosition(position);
        transaction.setTransactionType(request.transactionType());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setQuantity(request.quantity());
        transaction.setUnitPrice(request.unitPrice());
        transaction.setTotalAmount(request.totalAmount());

        transaction.setFees(request.fees() != null ? request.fees() : BigDecimal.ZERO);

        transaction.setTaxes(request.taxes() != null ? request.taxes() : BigDecimal.ZERO);

        transaction.setCurrency(normalizeCurrency(request.currency()));
        transaction.setReferenceNumber(normalize(request.referenceNumber()));
        transaction.setNotes(normalize(request.notes()));

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(savedTransaction);
    }

    public Page<TransactionResponse> getTransactions(String userId, Pageable pageable) {
        return transactionRepository
                .findAllByUser_UserId(userId, pageable)
                .map(TransactionResponse::new);
    }

    public Page<TransactionResponse> getTransactionsByPosition(String userId, UUID positionId, Pageable pageable) {
        return transactionRepository
                .findAllByUser_UserIdAndPosition_PositionId(userId, positionId, pageable)
                .map(TransactionResponse::new);
    }

    public Page<TransactionResponse> getTransactionsByType(String userId, TransactionType transactionType,
                                                           Pageable pageable) {
        return transactionRepository
                .findAllByUser_UserIdAndTransactionType(userId, transactionType, pageable)
                .map(TransactionResponse::new);
    }

    public Page<TransactionResponse> getTransactionsByPositionAndType(String userId, UUID positionId,
                                                                      TransactionType transactionType,
                                                                      Pageable pageable) {
        return transactionRepository
                .findAllByUser_UserIdAndPosition_PositionIdAndTransactionType(userId, positionId, transactionType, pageable)
                .map(TransactionResponse::new);
    }

    public TransactionResponse getTransaction(UUID transactionId, String userId) {
        Transaction transaction = findOwnedTransaction(transactionId, userId);
        return new TransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(UUID transactionId, String userId, UpdateTransactionRequest request) {
        Transaction transaction = findOwnedTransaction(transactionId, userId);

        transaction.setTransactionType(request.transactionType());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setQuantity(request.quantity());
        transaction.setUnitPrice(request.unitPrice());
        transaction.setTotalAmount(request.totalAmount());
        transaction.setFees(request.fees());
        transaction.setTaxes(request.taxes());
        transaction.setCurrency(normalizeCurrency(request.currency()));
        transaction.setReferenceNumber(normalize(request.referenceNumber()));
        transaction.setNotes(normalize(request.notes()));

        // Managed entity: JPA dirty checking persists the changes.
        return new TransactionResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(UUID transactionId, String userId) {
        Transaction transaction = findOwnedTransaction(transactionId, userId);
        transactionRepository.delete(transaction);
    }

    // handler methods
    private Transaction findOwnedTransaction(UUID transactionId, String userId) {
        return transactionRepository
                .findByTransactionIdAndUser_UserId(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    private HoldingPosition findOwnedPosition(UUID positionId, String userId) {
        return holdingPositionRepository
                .findByPositionIdAndUser_UserId(positionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding position not found"));
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            return "INR";
        }
        return currency.trim().toUpperCase(Locale.ROOT);
    }
}
