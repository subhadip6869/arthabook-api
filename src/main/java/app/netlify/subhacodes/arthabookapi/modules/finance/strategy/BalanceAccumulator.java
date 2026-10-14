package app.netlify.subhacodes.arthabookapi.modules.finance.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record BalanceAccumulator(BigDecimal quantity, BigDecimal costBasis, BigDecimal averageCost) {
    public static BalanceAccumulator empty() {
        return new BalanceAccumulator(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public BalanceAccumulator recalculateAverageCost() {
        BigDecimal avg = quantity.signum() == 0
                ? BigDecimal.ZERO
                : costBasis.divide(quantity, 8, RoundingMode.HALF_UP);
        return new BalanceAccumulator(quantity, costBasis, avg);
    }
}
