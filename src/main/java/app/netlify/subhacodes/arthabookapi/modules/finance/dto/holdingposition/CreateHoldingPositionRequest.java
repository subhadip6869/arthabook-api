package app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition;

import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.CreateInvestmentDetailRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateHoldingPositionRequest(
        @NotNull(message = "Holding ID is required")
        UUID holdingId,

        @NotNull(message = "Account ID is required")
        UUID accountId,

        @Size(max = 200, message = "Position name must be at most 200 characters long")
        String positionName,

        @Size(max = 100, message = "Folio number must be at most 100 characters long")
        String folioNumber,

        String notes,

        @Valid
        CreateInvestmentDetailRequest investmentDetail
) {
}
