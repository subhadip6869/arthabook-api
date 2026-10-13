package app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction;

import app.netlify.subhacodes.arthabookapi.common.validation.ValidCurrency;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateTransactionRequest(
        @NotNull(message = "Position ID is required")
        UUID positionId,

        @NotNull(message = "Transaction type is required")
        TransactionType transactionType,

        @NotNull(message = "Transaction date is required")
        OffsetDateTime transactionDate,

        @DecimalMin(value = "0.0", message = "Quantity must not be negative")
        @Digits(integer = 20, fraction = 10)
        BigDecimal quantity,

        @DecimalMin(value = "0.0", message = "Unit price must not be negative")
        @Digits(integer = 12, fraction = 8)
        BigDecimal unitPrice,

        @NotNull(message = "Total amount is required")
        @DecimalMin(value = "0.0", message = "Total amount must not be negative")
        @Digits(integer = 16, fraction = 4)
        BigDecimal totalAmount,

        @DecimalMin(value = "0.0", message = "Fees must not be negative")
        @Digits(integer = 16, fraction = 4)
        BigDecimal fees,

        @DecimalMin(value = "0.0", message = "Taxes must not be negative")
        @Digits(integer = 16, fraction = 4)
        BigDecimal taxes,

        @ValidCurrency
        String currency,

        @Size(max = 50)
        String referenceNumber,

        String notes
) {
}
