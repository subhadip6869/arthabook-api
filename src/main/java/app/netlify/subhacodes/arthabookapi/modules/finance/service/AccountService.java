package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.account.AccountResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.account.CreateAccountRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.account.UpdateAccountRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Account;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.AccountStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.AccountRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingPositionRepository;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import app.netlify.subhacodes.arthabookapi.modules.profile.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final HoldingPositionRepository positionRepository;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository,
            HoldingPositionRepository positionRepository
    ) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.positionRepository = positionRepository;
    }

    @Transactional
    public AccountResponse createAccount(String userId, CreateAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account = new Account();

        account.setUser(user);
        account.setAccountName(request.accountName().trim());
        account.setAccountType(request.accountType());
        account.setProviderName(normalize(request.providerName()));
        account.setAccountNumber(normalize(request.accountNumber()));
        account.setCurrency(request.currency() == null ? "INR" : request.currency());
        account.setNotes(normalize(request.notes()));

        Account savedAccount = accountRepository.saveAndFlush(account);
        return new AccountResponse(savedAccount);
    }

    public Page<AccountResponse> getAccounts(String userId, Pageable pageable) {
        return accountRepository.findAllByUser_UserId(userId, pageable)
                .map(AccountResponse::new);
    }

    public AccountResponse getAccount(UUID accountId, String userId) {
        Account account = findOwnedAccount(accountId, userId);
        return new AccountResponse(account);
    }

    @Transactional
    public AccountResponse updateAccount(UUID accountId, String userId, UpdateAccountRequest request
    ) {
        Account account = findOwnedAccount(accountId, userId);

        account.setAccountName(request.accountName().trim());
        account.setAccountType(request.accountType());
        account.setProviderName(normalize(request.providerName()));
        account.setAccountNumber(normalize(request.accountNumber()));
        account.setCurrency(request.currency());
        account.setNotes(normalize(request.notes()));

        accountRepository.flush();
        
        return new AccountResponse(account);
    }

    @Transactional
    public AccountResponse updateAccountStatus(UUID accountId, String userId, AccountStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Account status must not be null");
        }

        Account account = findOwnedAccount(accountId, userId);
        account.setAccountStatus(status);
        accountRepository.flush();

        return new AccountResponse(account);
    }

    @Transactional
    public void deleteAccount(UUID accountId, String userId) {
        Account account = findOwnedAccount(accountId, userId);

        // Guard: Prevent deletion if any holding positions are associated with this account
        // Check in HoldingPositionRepository:
        if (positionRepository.existsByAccount_AccountIdAndUser_UserId(accountId, userId)) {
            throw new IllegalStateException("Cannot delete account with existing holding positions. Close the account instead.");
        }
        accountRepository.delete(account);
    }

    // handler methods
    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private Account findOwnedAccount(UUID accountId, String userId) {
        return accountRepository.findByAccountIdAndUser_UserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
    }
}
