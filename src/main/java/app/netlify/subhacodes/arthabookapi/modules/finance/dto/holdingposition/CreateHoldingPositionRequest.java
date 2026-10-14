package app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

        String notes
) {
}
