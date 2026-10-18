package app.netlify.subhacodes.arthabookapi.modules.finance.controller;

import app.netlify.subhacodes.arthabookapi.common.security.FirebaseUserPrincipal;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition.CreateHoldingPositionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition.HoldingPositionResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition.UpdateHoldingPositionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.service.HoldingPositionService;
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
@RequestMapping("/api/v1/finance/positions")
public class HoldingPositionController {
    private final HoldingPositionService holdingPositionService;

    public HoldingPositionController(HoldingPositionService holdingPositionService) {
        this.holdingPositionService = holdingPositionService;
    }

    @PostMapping
    public ResponseEntity<HoldingPositionResponse> createPosition(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @Valid @RequestBody CreateHoldingPositionRequest request) {
        HoldingPositionResponse response = holdingPositionService.createPosition(principal.uid(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<HoldingPositionResponse>> getPositions(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @RequestParam(required = false) UUID holdingId,
            @RequestParam(required = false) UUID accountId,
            @RequestParam(required = false) HoldingStatus status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        if (holdingId != null && status != null) {
            return ResponseEntity.ok(holdingPositionService.getPositionsByHoldingAndStatus(holdingId, principal.uid(), status, pageable));
        } else if (accountId != null && status != null) {
            return ResponseEntity.ok(holdingPositionService.getPositionsByAccountAndStatus(accountId, principal.uid(), status, pageable));
        } else if (holdingId != null) {
            return ResponseEntity.ok(holdingPositionService.getPositionsByHolding(holdingId, principal.uid(), pageable));
        } else if (accountId != null) {
            return ResponseEntity.ok(holdingPositionService.getPositionsByAccount(accountId, principal.uid(), pageable));
        } else if (status != null) {
            return ResponseEntity.ok(holdingPositionService.getPositionsByStatus(principal.uid(), status, pageable));
        }
        return ResponseEntity.ok(holdingPositionService.getPositions(principal.uid(), pageable));
    }

    @GetMapping("/{positionId}")
    public ResponseEntity<HoldingPositionResponse> getPosition(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID positionId) {
        return ResponseEntity.ok(holdingPositionService.getPosition(positionId, principal.uid()));
    }

    @PutMapping("/{positionId}")
    public ResponseEntity<HoldingPositionResponse> updatePosition(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID positionId,
            @Valid @RequestBody UpdateHoldingPositionRequest request) {
        return ResponseEntity.ok(holdingPositionService.updatePosition(positionId, principal.uid(), request));
    }

    @PatchMapping("/{positionId}/status")
    public ResponseEntity<HoldingPositionResponse> updatePositionStatus(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID positionId,
            @RequestParam HoldingStatus status) {
        return ResponseEntity.ok(holdingPositionService.updatePositionStatus(positionId, principal.uid(), status));
    }

    @DeleteMapping("/{positionId}")
    public ResponseEntity<Void> deletePosition(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @PathVariable UUID positionId) {
        holdingPositionService.deletePosition(positionId, principal.uid());
        return ResponseEntity.noContent().build();
    }
}
