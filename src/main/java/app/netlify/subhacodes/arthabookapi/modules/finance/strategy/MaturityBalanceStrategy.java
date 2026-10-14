package app.netlify.subhacodes.arthabookapi.modules.finance.strategy;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MaturityBalanceStrategy implements TransactionBalanceStrategy {
    @Override
    public boolean supports(TransactionType type) {
        return type == TransactionType.MATURITY;
    }

    @Override
    public BalanceAccumulator apply(BalanceAccumulator current, Transaction tx) {
        return new BalanceAccumulator(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
