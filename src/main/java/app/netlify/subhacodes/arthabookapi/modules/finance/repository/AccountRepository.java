package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAllByUser_UserId(String userId);

    Optional<Account> findByAccountIdAndUser_UserId(
            UUID accountId, String userId
    );

    boolean existsByAccountIdAndUser_UserId(
            UUID accountId, String userId
    );
}
