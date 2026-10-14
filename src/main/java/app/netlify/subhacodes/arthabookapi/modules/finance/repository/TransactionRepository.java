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
    Page<Transaction> findAllByUser_UserId(String userId, Pageable pageable);

    Page<Transaction> findAllByUser_UserIdAndPosition_PositionId(String userId, UUID positionId, Pageable pageable);

    Page<Transaction> findAllByUser_UserIdAndTransactionType(String userId, TransactionType transactionType,
                                                             Pageable pageable);

    Page<Transaction> findAllByUser_UserIdAndPosition_PositionIdAndTransactionType(String userId, UUID positionId,
                                                                                   TransactionType transactionType,
                                                                                   Pageable pageable);

    Optional<Transaction> findByTransactionIdAndUser_UserId(UUID transactionId, String userId);


    List<Transaction> findAllByUser_UserIdAndPosition_PositionIdOrderByTransactionDateAscCreatedAtAscTransactionIdAsc(
            String userId,
            UUID positionId
    );


    boolean existsByTransactionIdAndUser_UserId(UUID transactionId, String userId);
}
