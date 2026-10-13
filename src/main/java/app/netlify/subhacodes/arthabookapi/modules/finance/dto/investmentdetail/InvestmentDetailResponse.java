package app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.InvestmentDetail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record InvestmentDetailResponse(
        UUID positionId,
        String issuerName,
        String instrumentCode,
        String isin,
        String exchange,
        BigDecimal interestRate,
        LocalDate investmentDate,
        LocalDate maturityDate,
        BigDecimal maturityAmount,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {
    public InvestmentDetailResponse(InvestmentDetail detail) {
        this(
                detail.getPositionId(),
                detail.getIssuerName(),
                detail.getInstrumentCode(),
                detail.getIsin(),
                detail.getExchange(),
                detail.getInterestRate(),
                detail.getInvestmentDate(),
                detail.getMaturityDate(),
                detail.getMaturityAmount(),
                detail.getCreatedAt(),
                detail.getUpdatedAt(),
                detail.getVersion()
        );
    }
}
