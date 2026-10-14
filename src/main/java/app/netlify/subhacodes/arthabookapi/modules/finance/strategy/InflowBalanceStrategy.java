package app.netlify.subhacodes.arthabookapi.modules.finance.strategy;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class InflowBalanceStrategy implements TransactionBalanceStrategy {
    private static final Set<TransactionType> SUPPORTED_TYPES = Set.of(
            TransactionType.BUY,
            TransactionType.TRANSFER_IN
    );

    @Override
    public boolean supports(TransactionType type) {
        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public BalanceAccumulator apply(BalanceAccumulator current, Transaction tx) {
        BigDecimal txQuantity = tx.getQuantity() != null ? tx.getQuantity() : BigDecimal.ZERO;
        BigDecimal totalAmount = tx.getTotalAmount() != null ? tx.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal fees = tx.getFees() != null ? tx.getFees() : BigDecimal.ZERO;
        BigDecimal taxes = tx.getTaxes() != null ? tx.getTaxes() : BigDecimal.ZERO;

        if (txQuantity.signum() <= 0) {
            throw new IllegalArgumentException(tx.getTransactionType() + " quantity must be greater than zero");
        }

        BigDecimal newCostBasis = current.costBasis()
                .add(totalAmount)
                .add(fees)
                .add(taxes);

        BigDecimal newQuantity = current.quantity().add(txQuantity);

        return new BalanceAccumulator(newQuantity, newCostBasis, current.averageCost())
                .recalculateAverageCost();
    }
}
