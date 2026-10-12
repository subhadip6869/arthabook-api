package app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateHoldingPositionRequest(
        @NotNull
        UUID holdingId,

        @NotNull
        UUID accountId,

        @Size(max = 200)
        String positionName,

        @Size(max = 100)
        String folioNumber,

        @NotNull
        @DecimalMin("0.0")
        @Digits(integer = 20, fraction = 10)
        BigDecimal quantity,

        @NotNull
        @DecimalMin("0.0")
        @Digits(integer = 12, fraction = 8)
        BigDecimal averageCost,

        @NotNull
        @DecimalMin("0.0")
        @Digits(integer = 16, fraction = 4)
        BigDecimal costBasis,

        String notes
) {
}
