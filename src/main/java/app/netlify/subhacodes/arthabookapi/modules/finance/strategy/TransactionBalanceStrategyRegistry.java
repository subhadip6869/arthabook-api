package app.netlify.subhacodes.arthabookapi.modules.finance.strategy;

import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionBalanceStrategyRegistry {
    private final List<TransactionBalanceStrategy> strategies;

    public TransactionBalanceStrategyRegistry(List<TransactionBalanceStrategy> strategies) {
        this.strategies = strategies;
    }

    public TransactionBalanceStrategy getStrategy(TransactionType type) {
        return strategies.stream()
                .filter(s -> s.supports(type))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("No calculation strategy registered for " + type));
    }
}
