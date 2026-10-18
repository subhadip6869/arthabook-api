package app.netlify.subhacodes.arthabookapi.modules.finance.controller;

import app.netlify.subhacodes.arthabookapi.common.security.FirebaseUserPrincipal;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.account.AccountResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.account.CreateAccountRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.account.UpdateAccountRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.AccountStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @Valid @RequestBody CreateAccountRequest accountRequest) {
        AccountResponse response = accountService.createAccount(principal.uid(), accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAccounts(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AccountResponse> accounts = accountService.getAccounts(principal.uid(), pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID accountId) {
        AccountResponse response = accountService.getAccount(accountId, principal.uid());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID accountId,
            @Valid @RequestBody UpdateAccountRequest request) {
        AccountResponse response = accountService.updateAccount(accountId, principal.uid(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{accountId}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID accountId,
            @RequestParam AccountStatus status) {
        AccountResponse response = accountService.updateAccountStatus(accountId, principal.uid(), status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID accountId) {
        accountService.deleteAccount(accountId, principal.uid());
        return ResponseEntity.noContent().build();
    }
}
