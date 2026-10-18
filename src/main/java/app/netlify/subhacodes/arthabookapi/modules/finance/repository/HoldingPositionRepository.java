package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HoldingPositionRepository extends JpaRepository<HoldingPosition, UUID> {
    Page<HoldingPosition> findAllByUser_UserId(String userId, Pageable pageable);

    Page<HoldingPosition> findAllByUser_UserIdAndHoldingStatus(
            String userId,
            HoldingStatus holdingStatus,
            Pageable pageable
    );

    Page<HoldingPosition> findAllByHolding_HoldingIdAndUser_UserId(
            UUID holdingId,
            String userId,
            Pageable pageable
    );

    Page<HoldingPosition> findAllByAccount_AccountIdAndUser_UserId(
            UUID accountId,
            String userId,
            Pageable pageable
    );

    Page<HoldingPosition> findAllByHolding_HoldingIdAndUser_UserIdAndHoldingStatus(
            UUID holdingId,
            String userId,
            HoldingStatus holdingStatus,
            Pageable pageable
    );

    Page<HoldingPosition> findAllByAccount_AccountIdAndUser_UserIdAndHoldingStatus(
            UUID accountId,
            String userId,
            HoldingStatus holdingStatus,
            Pageable pageable
    );

    Optional<HoldingPosition> findByPositionIdAndUser_UserId(
            UUID positionId,
            String userId
    );

    boolean existsByPositionIdAndUser_UserId(
            UUID positionId,
            String userId
    );

    boolean existsByAccount_AccountIdAndUser_UserId(UUID accountId, String userId);

    boolean existsByHolding_HoldingIdAndUser_UserId(UUID holdingId, String userId);
}
