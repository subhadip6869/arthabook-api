package app.netlify.subhacodes.arthabookapi.modules.finance.repository;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findAllByUser_UserIdOrderByTransactionDateDesc(
            String userId, Pageable pageable
    );

    List<Transaction> findAllByPosition_PositionIdAndUser_UserIdOrderByTransactionDateDesc(
            UUID positionId, String userId
    );

    List<Transaction> findAllByUser_UserIdAndTransactionType(
            String userId, TransactionType transactionType
    );

    Optional<Transaction> findByTransactionIdAndUser_UserId(
            UUID transactionId, String userId
    );

    boolean existsByTransactionIdAndUser_UserId(
            UUID transactionId, String userId
    );
}
