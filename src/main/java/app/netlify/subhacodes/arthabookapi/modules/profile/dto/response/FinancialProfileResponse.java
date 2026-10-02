package app.netlify.subhacodes.arthabookapi.modules.profile.dto.response;

import app.netlify.subhacodes.arthabookapi.modules.profile.entity.FinancialProfile;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.AnnualIncomeRange;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.Occupation;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.RiskAppetite;

public record FinancialProfileResponse(
        String userId,
        Occupation occupation,
        AnnualIncomeRange annualIncomeRange,
        RiskAppetite riskAppetite,
        String baseCurrency
) {
    public FinancialProfileResponse(FinancialProfile profile) {
        this(
                profile.getUserId(),
                profile.getOccupation(),
                profile.getAnnualIncomeRange(),
                profile.getRiskAppetite(),
                profile.getBaseCurrency()
        );
    }
}
