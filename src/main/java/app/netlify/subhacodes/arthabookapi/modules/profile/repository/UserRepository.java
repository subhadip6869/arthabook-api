package app.netlify.subhacodes.arthabookapi.modules.profile.repository;

import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.financialProfile
            WHERE u.userId = :userId
            """)
    Optional<User> findByIdWithFinancialProfile(@Param("userId" ) String userId);
}
