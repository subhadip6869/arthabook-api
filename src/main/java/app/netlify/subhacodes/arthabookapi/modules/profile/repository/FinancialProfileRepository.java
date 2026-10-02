package app.netlify.subhacodes.arthabookapi.modules.profile.repository;

import app.netlify.subhacodes.arthabookapi.modules.profile.entity.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, String> {
}
