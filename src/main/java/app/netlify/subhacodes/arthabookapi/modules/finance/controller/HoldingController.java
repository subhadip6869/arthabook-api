package app.netlify.subhacodes.arthabookapi.modules.finance.controller;

import app.netlify.subhacodes.arthabookapi.common.security.FirebaseUserPrincipal;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding.CreateHoldingRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding.HoldingResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding.UpdateHoldingRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;
import app.netlify.subhacodes.arthabookapi.modules.finance.service.HoldingService;
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
@RequestMapping("/api/v1/finance/holdings")
public class HoldingController {
    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @PostMapping
    public ResponseEntity<HoldingResponse> createHolding(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @Valid @RequestBody CreateHoldingRequest request) {
        HoldingResponse response = holdingService.createHolding(principal.uid(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<HoldingResponse>> getHoldings(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @RequestParam(required = false) HoldingType type,
            @RequestParam(required = false) HoldingStatus status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        if (type != null && status != null) {
            return ResponseEntity.ok(holdingService.getHoldingsByTypeAndStatus(principal.uid(), type, status, pageable));
        } else if (type != null) {
            return ResponseEntity.ok(holdingService.getHoldingsByType(principal.uid(), type, pageable));
        } else if (status != null) {
            return ResponseEntity.ok(holdingService.getHoldingsByStatus(principal.uid(), status, pageable));
        }
        return ResponseEntity.ok(holdingService.getHoldings(principal.uid(), pageable));
    }

    @GetMapping("/{holdingId}")
    public ResponseEntity<HoldingResponse> getHolding(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID holdingId) {
        return ResponseEntity.ok(holdingService.getHolding(holdingId, principal.uid()));
    }

    @PutMapping("/{holdingId}")
    public ResponseEntity<HoldingResponse> updateHolding(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID holdingId,
            @Valid @RequestBody UpdateHoldingRequest request) {
        return ResponseEntity.ok(holdingService.updateHolding(holdingId, principal.uid(), request));
    }

    @PatchMapping("/{holdingId}/status")
    public ResponseEntity<HoldingResponse> updateHoldingStatus(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID holdingId,
            @RequestParam HoldingStatus status) {
        return ResponseEntity.ok(holdingService.updateHoldingStatus(holdingId, principal.uid(), status));
    }

    @DeleteMapping("/{holdingId}")
    public ResponseEntity<Void> deleteHolding(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID holdingId) {
        holdingService.deleteHolding(holdingId, principal.uid());
        return ResponseEntity.noContent().build();
    }
}
