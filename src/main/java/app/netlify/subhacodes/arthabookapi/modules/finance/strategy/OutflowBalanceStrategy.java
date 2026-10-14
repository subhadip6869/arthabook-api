package app.netlify.subhacodes.arthabookapi.modules.finance.strategy;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class OutflowBalanceStrategy implements TransactionBalanceStrategy {
    private static final Set<TransactionType> SUPPORTED_TYPES = Set.of(
            TransactionType.SELL,
            TransactionType.TRANSFER_OUT
    );

    @Override
    public boolean supports(TransactionType type) {
        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public BalanceAccumulator apply(BalanceAccumulator current, Transaction tx) {
        BigDecimal txQuantity = tx.getQuantity() != null ? tx.getQuantity() : BigDecimal.ZERO;

        if (txQuantity.signum() <= 0) {
            throw new IllegalArgumentException(tx.getTransactionType() + " quantity must be greater than zero");
        }

        if (txQuantity.compareTo(current.quantity()) > 0) {
            throw new IllegalArgumentException(
                    "Insufficient quantity for " + tx.getTransactionType() + " transaction: " + tx.getTransactionId()
            );
        }

        BigDecimal soldCost = current.averageCost().multiply(txQuantity);
        BigDecimal newQuantity = current.quantity().subtract(txQuantity);
        BigDecimal newCostBasis = current.costBasis().subtract(soldCost).max(BigDecimal.ZERO);

        return new BalanceAccumulator(newQuantity, newCostBasis, current.averageCost())
                .recalculateAverageCost();
    }
}
