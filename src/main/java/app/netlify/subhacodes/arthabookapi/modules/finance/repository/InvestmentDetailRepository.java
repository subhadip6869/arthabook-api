package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.InvestmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvestmentDetailRepository extends JpaRepository<InvestmentDetail, UUID> {
}
