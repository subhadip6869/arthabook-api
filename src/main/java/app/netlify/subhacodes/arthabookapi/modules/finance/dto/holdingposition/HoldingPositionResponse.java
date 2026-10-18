package app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition;

import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.InvestmentDetailResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record HoldingPositionResponse(
        UUID positionId,

        UUID holdingId,
        String holdingName,

        UUID accountId,
        String accountName,

        String positionName,
        String folioNumber,

        BigDecimal quantity,
        BigDecimal averageCost,
        BigDecimal costBasis,

        HoldingStatus holdingStatus,
        String notes,

        InvestmentDetailResponse investmentDetail,

        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {
    public HoldingPositionResponse(HoldingPosition position, InvestmentDetailResponse investmentDetail) {
        this(
                position.getPositionId(),

                position.getHolding().getHoldingId(),
                position.getHolding().getHoldingName(),

                position.getAccount().getAccountId(),
                position.getAccount().getAccountName(),

                position.getPositionName(),
                position.getFolioNumber(),

                position.getQuantity(),
                position.getAverageCost(),
                position.getCostBasis(),

                position.getHoldingStatus(),
                position.getNotes(),

                investmentDetail,

                position.getCreatedAt(),
                position.getUpdatedAt(),
                position.getVersion()
        );
    }

    public HoldingPositionResponse(HoldingPosition position) {
        this(
                position,
                position.getInvestmentDetail() != null
                        ? new InvestmentDetailResponse(position.getInvestmentDetail())
                        : null
        );
    }
}
