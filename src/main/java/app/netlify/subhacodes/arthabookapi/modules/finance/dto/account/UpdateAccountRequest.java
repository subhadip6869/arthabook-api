package app.netlify.subhacodes.arthabookapi.modules.finance.dto.account;

import app.netlify.subhacodes.arthabookapi.common.validation.ValidCurrency;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAccountRequest(
        @NotBlank
        @Size(max = 150)
        String accountName,

        @NotNull
        AccountType accountType,

        @Size(max = 150)
        String providerName,

        @Size(max = 50)
        String accountNumber,

        @NotNull
        @ValidCurrency(message = "Currency must be a 3-letter uppercase code")
        String currency,

        String notes
) {
}
