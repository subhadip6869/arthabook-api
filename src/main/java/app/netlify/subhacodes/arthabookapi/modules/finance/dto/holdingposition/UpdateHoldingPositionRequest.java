package app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition;

import jakarta.validation.constraints.Size;

public record UpdateHoldingPositionRequest(
        @Size(max = 200)
        String positionName,

        @Size(max = 100)
        String folioNumber,

        String notes
) {
}
