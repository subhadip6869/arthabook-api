package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Holding;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HoldingRepository extends JpaRepository<Holding, UUID> {
    List<Holding> findAllByUser_UserId(String userId);

    List<Holding> findAllByUser_UserIdAndHoldingStatus(
            String userId, HoldingStatus holdingStatus
    );

    List<Holding> findAllByUser_UserIdAndHoldingType(
            String userId, HoldingType holdingType
    );

    Optional<Holding> findByHoldingIdAndUser_UserId(
            UUID holdingId, String userId
    );

    boolean existsByHoldingIdAndUser_UserId(
            UUID holdingId, String userId
    );
}
