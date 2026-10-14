package app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInvestmentDetailRequest(
        @Size(max = 150)
        String issuerName,

        @Size(max = 100)
        String instrumentCode,

        @Size(max = 12)
        String isin,

        @Size(max = 50)
        String exchange,

        @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
        @Digits(integer = 3, fraction = 4)
        BigDecimal interestRate,

        @PastOrPresent
        LocalDate investmentDate,

        LocalDate maturityDate,

        @DecimalMin(value = "0.0", message = "Maturity amount cannot be negative")
        @Digits(integer = 16, fraction = 4)
        BigDecimal maturityAmount
) {
    @AssertTrue(message = "Maturity date must be on or after investment date")
    public boolean isValidDateRange() {
        if (investmentDate == null || maturityDate == null) {
            return true;
        }
        return !maturityDate.isBefore(investmentDate);
    }
}
