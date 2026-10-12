package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Holding;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HoldingRepository extends JpaRepository<Holding, UUID> {
    Page<Holding> findAllByUser_UserId(String userId, Pageable pageable);

    Page<Holding> findAllByUser_UserIdAndHoldingStatus(String userId, HoldingStatus holdingStatus, Pageable pageable);

    Page<Holding> findAllByUser_UserIdAndHoldingType(String userId, HoldingType holdingType, Pageable pageable);

    Page<Holding> findAllByUser_UserIdAndHoldingTypeAndHoldingStatus(
            String userId,
            HoldingType holdingType,
            HoldingStatus holdingStatus,
            Pageable pageable
    );

    Optional<Holding> findByHoldingIdAndUser_UserId(UUID holdingId, String userId);

    boolean existsByHoldingIdAndUser_UserId(UUID holdingId, String userId);
}
