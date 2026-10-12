package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Page<Account> findAllByUser_UserId(String userId, Pageable pageable);

    Optional<Account> findByAccountIdAndUser_UserId(UUID accountId, String userId);

    boolean existsByAccountIdAndUser_UserId(UUID accountId, String userId);
}
