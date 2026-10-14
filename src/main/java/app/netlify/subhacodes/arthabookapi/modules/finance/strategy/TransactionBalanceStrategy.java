package app.netlify.subhacodes.arthabookapi.modules.finance.strategy;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Transaction;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;

public interface TransactionBalanceStrategy {
    boolean supports(TransactionType type);

    BalanceAccumulator apply(BalanceAccumulator current, Transaction tx);
}
