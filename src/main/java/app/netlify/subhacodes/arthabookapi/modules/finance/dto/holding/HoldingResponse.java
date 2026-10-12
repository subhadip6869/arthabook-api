package app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Holding;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record HoldingResponse(
        UUID holdingId,
        String holdingName,
        HoldingType holdingType,
        String currency,
        HoldingStatus holdingStatus,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {
    public HoldingResponse(Holding holding) {
        this(
                holding.getHoldingId(),
                holding.getHoldingName(),
                holding.getHoldingType(),
                holding.getCurrency(),
                holding.getHoldingStatus(),
                holding.getNotes(),
                holding.getCreatedAt(),
                holding.getUpdatedAt(),
                holding.getVersion()
        );
    }
}
