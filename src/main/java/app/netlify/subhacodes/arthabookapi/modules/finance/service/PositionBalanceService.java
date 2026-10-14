package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingPositionRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.TransactionRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.strategy.BalanceAccumulator;
import app.netlify.subhacodes.arthabookapi.modules.finance.strategy.TransactionBalanceStrategy;
import app.netlify.subhacodes.arthabookapi.modules.finance.strategy.TransactionBalanceStrategyRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class PositionBalanceService {
    private final HoldingPositionRepository holdingPositionRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionBalanceStrategyRegistry strategyRegistry;

    public PositionBalanceService(
            HoldingPositionRepository holdingPositionRepository,
            TransactionRepository transactionRepository,
            TransactionBalanceStrategyRegistry strategyRegistry
    ) {
        this.holdingPositionRepository = holdingPositionRepository;
        this.transactionRepository = transactionRepository;
        this.strategyRegistry = strategyRegistry;
    }

    @Transactional
    public void recalculatePosition(UUID positionId, String userId) {
        HoldingPosition position = holdingPositionRepository
                .findByPositionIdAndUser_UserId(positionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding position not found"));

        List<Transaction> transactions = transactionRepository
                .findAllByUser_UserIdAndPosition_PositionIdOrderByTransactionDateAscCreatedAtAscTransactionIdAsc(userId, positionId);

        BalanceAccumulator accumulator = BalanceAccumulator.empty();

        for (Transaction tx : transactions) {
            TransactionBalanceStrategy strategy = strategyRegistry.getStrategy(tx.getTransactionType());
            accumulator = strategy.apply(accumulator, tx);
        }

        position.setQuantity(accumulator.quantity());
        position.setCostBasis(accumulator.costBasis().setScale(4, RoundingMode.HALF_UP));
        position.setAverageCost(accumulator.averageCost().setScale(8, RoundingMode.HALF_UP));
    }
}
