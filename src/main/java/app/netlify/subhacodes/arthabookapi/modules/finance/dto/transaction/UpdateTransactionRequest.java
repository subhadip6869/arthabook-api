package app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction;

import app.netlify.subhacodes.arthabookapi.common.validation.ValidCurrency;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record UpdateTransactionRequest(
        @NotNull()
        TransactionType transactionType,

        @NotNull
        OffsetDateTime transactionDate,

        @DecimalMin("0.0")
        @Digits(integer = 20, fraction = 10)
        BigDecimal quantity,

        @DecimalMin("0.0")
        @Digits(integer = 12, fraction = 8)
        BigDecimal unitPrice,

        @NotNull
        @DecimalMin("0.0")
        @Digits(integer = 16, fraction = 4)
        BigDecimal totalAmount,

        @NotNull
        @DecimalMin("0.0")
        @Digits(integer = 16, fraction = 4)
        BigDecimal fees,

        @NotNull
        @DecimalMin("0.0")
        @Digits(integer = 16, fraction = 4)
        BigDecimal taxes,

        @NotNull
        @ValidCurrency
        String currency,

        @Size(max = 50)
        String referenceNumber,

        String notes
) {
}
