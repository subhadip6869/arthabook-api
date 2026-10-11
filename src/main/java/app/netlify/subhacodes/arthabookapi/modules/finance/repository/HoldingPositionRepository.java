package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HoldingPositionRepository extends JpaRepository<HoldingPosition, UUID> {
    List<HoldingPosition> findAllByUser_UserId(String userId);

    List<HoldingPosition> findAllByUser_UserIdAndHoldingStatus(
            String userId, HoldingStatus holdingStatus
    );

    List<HoldingPosition> findAllByHolding_HoldingIdAndUser_UserId(
            UUID holdingId, String userId
    );

    List<HoldingPosition> findAllByAccount_AccountIdAndUser_UserId(
            UUID accountId, String userId
    );

    Optional<HoldingPosition> findByPositionIdAndUser_UserId(
            UUID positionId, String userId
    );

    boolean existsByPositionIdAndUser_UserId(
            UUID positionId, String userId
    );
}
