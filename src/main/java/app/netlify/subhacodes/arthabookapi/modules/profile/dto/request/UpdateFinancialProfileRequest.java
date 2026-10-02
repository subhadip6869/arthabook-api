package app.netlify.subhacodes.arthabookapi.modules.profile.dto.request;

import app.netlify.subhacodes.arthabookapi.common.validation.ValidCurrency;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.AnnualIncomeRange;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.Occupation;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.RiskAppetite;

public record UpdateFinancialProfileRequest(
        Occupation occupation,
        AnnualIncomeRange annualIncomeRange,
        RiskAppetite riskAppetite,
        @ValidCurrency String baseCurrency
) {
}
