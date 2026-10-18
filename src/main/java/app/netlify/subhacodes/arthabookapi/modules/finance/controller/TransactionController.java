package app.netlify.subhacodes.arthabookapi.modules.finance.controller;

import app.netlify.subhacodes.arthabookapi.common.security.FirebaseUserPrincipal;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.CreateTransactionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.TransactionResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.transaction.UpdateTransactionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import app.netlify.subhacodes.arthabookapi.modules.finance.service.TransactionService;
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
@RequestMapping("/api/v1/finance/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @Valid @RequestBody CreateTransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(principal.uid(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @RequestParam(required = false) UUID positionId,
            @RequestParam(required = false) TransactionType type,
            @PageableDefault(sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {

        if (positionId != null && type != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByPositionAndType(principal.uid(), positionId, type, pageable));
        } else if (positionId != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByPosition(principal.uid(), positionId, pageable));
        } else if (type != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByType(principal.uid(), type, pageable));
        }
        return ResponseEntity.ok(transactionService.getTransactions(principal.uid(), pageable));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID transactionId) {
        return ResponseEntity.ok(transactionService.getTransaction(transactionId, principal.uid()));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID transactionId,
            @Valid @RequestBody UpdateTransactionRequest request) {
        return ResponseEntity.ok(transactionService.updateTransaction(transactionId, principal.uid(), request));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID transactionId) {
        transactionService.deleteTransaction(transactionId, principal.uid());
        return ResponseEntity.noContent().build();
    }
}
