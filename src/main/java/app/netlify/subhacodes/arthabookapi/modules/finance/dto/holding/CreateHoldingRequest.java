package app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding;

import app.netlify.subhacodes.arthabookapi.common.validation.ValidCurrency;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHoldingRequest(
        @NotBlank
        @Size(max = 150)
        String holdingName,

        @NotNull
        HoldingType holdingType,

        @ValidCurrency
        String currency,

        String notes
) {
}
