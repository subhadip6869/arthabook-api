package app.netlify.subhacodes.arthabookapi.modules.finance.strategy;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NoOpBalanceStrategy implements TransactionBalanceStrategy {
    private static final Set<TransactionType> SUPPORTED_TYPES = Set.of(
            TransactionType.DEPOSIT,
            TransactionType.WITHDRAWAL,
            TransactionType.DIVIDEND,
            TransactionType.INTEREST,
            TransactionType.FEE,
            TransactionType.TAX,
            TransactionType.OTHER
    );

    @Override
    public boolean supports(TransactionType type) {
        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public BalanceAccumulator apply(BalanceAccumulator current, Transaction tx) {
        return current;
    }
}
