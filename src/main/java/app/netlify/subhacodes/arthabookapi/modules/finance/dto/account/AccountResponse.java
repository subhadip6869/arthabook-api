package app.netlify.subhacodes.arthabookapi.modules.finance.dto.account;

import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Account;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.AccountStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.AccountType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID accountId,
        String accountName,
        AccountType accountType,
        String providerName,
        String accountNumber,
        String currency,
        AccountStatus accountStatus,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {
    public AccountResponse(Account account) {
        this(
                account.getAccountId(),
                account.getAccountName(),
                account.getAccountType(),
                account.getProviderName(),
                account.getAccountNumber(),
                account.getCurrency(),
                account.getAccountStatus(),
                account.getNotes(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getVersion()
        );
    }
}
