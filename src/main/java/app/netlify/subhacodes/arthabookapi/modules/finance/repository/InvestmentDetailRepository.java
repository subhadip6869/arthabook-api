package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.InvestmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestmentDetailRepository extends JpaRepository<InvestmentDetail, UUID> {
    Optional<InvestmentDetail> findByPosition_PositionIdAndPosition_User_UserId(
            UUID positionId, String userId
    );

    List<InvestmentDetail> findAllByPosition_User_UserId(String userId);

    boolean existsByPosition_PositionId(UUID positionId);
}
