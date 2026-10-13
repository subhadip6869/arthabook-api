package app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID transactionId,
        UUID positionId,
        String positionName,
        UUID holdingId,
        String holdingName,
        UUID accountId,
        String accountName,
        TransactionType transactionType,
        OffsetDateTime transactionDate,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        BigDecimal fees,
        BigDecimal taxes,
        String currency,
        String referenceNumber,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {
    public TransactionResponse(Transaction tx) {
        this(
                tx.getTransactionId(),
                tx.getPosition().getPositionId(),
                tx.getPosition().getPositionName(),
                tx.getPosition().getHolding().getHoldingId(),
                tx.getPosition().getHolding().getHoldingName(),
                tx.getPosition().getAccount().getAccountId(),
                tx.getPosition().getAccount().getAccountName(),
                tx.getTransactionType(),
                tx.getTransactionDate(),
                tx.getQuantity(),
                tx.getUnitPrice(),
                tx.getTotalAmount(),
                tx.getFees(),
                tx.getTaxes(),
                tx.getCurrency(),
                tx.getReferenceNumber(),
                tx.getNotes(),
                tx.getCreatedAt(),
                tx.getUpdatedAt(),
                tx.getVersion()
        );
    }
}
