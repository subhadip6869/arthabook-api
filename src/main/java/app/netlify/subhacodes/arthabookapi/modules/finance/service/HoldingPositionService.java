package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition.CreateHoldingPositionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition.HoldingPositionResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holdingposition.UpdateHoldingPositionRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Account;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Holding;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.AccountRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingPositionRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class HoldingPositionService {
    private final HoldingPositionRepository holdingPositionRepository;
    private final HoldingRepository holdingRepository;
    private final AccountRepository accountRepository;

    public HoldingPositionService(
            HoldingPositionRepository holdingPositionRepository,
            HoldingRepository holdingRepository,
            AccountRepository accountRepository) {

        this.holdingPositionRepository = holdingPositionRepository;
        this.holdingRepository = holdingRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public HoldingPositionResponse createPosition(String userId, CreateHoldingPositionRequest request) {
        Holding holding = holdingRepository
                .findByHoldingIdAndUser_UserId(request.holdingId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found"));

        Account account = accountRepository
                .findByAccountIdAndUser_UserId(request.accountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        HoldingPosition position = new HoldingPosition();

        position.setUser(holding.getUser());
        position.setHolding(holding);
        position.setAccount(account);

        position.setPositionName(normalize(request.positionName()));
        position.setFolioNumber(normalize(request.folioNumber()));
        position.setNotes(normalize(request.notes()));

        HoldingPosition savedPosition = holdingPositionRepository.save(position);

        return new HoldingPositionResponse(savedPosition);
    }

    public Page<HoldingPositionResponse> getPositions(String userId, Pageable pageable) {
        return holdingPositionRepository
                .findAllByUser_UserId(userId, pageable)
                .map(HoldingPositionResponse::new);
    }

    public Page<HoldingPositionResponse> getPositionsByStatus(String userId, HoldingStatus status, Pageable pageable) {
        return holdingPositionRepository
                .findAllByUser_UserIdAndHoldingStatus(userId, status, pageable)
                .map(HoldingPositionResponse::new);
    }

    public Page<HoldingPositionResponse> getPositionsByHolding(UUID holdingId, String userId, Pageable pageable) {
        // The query also checks ownership, preventing cross-user access.
        return holdingPositionRepository
                .findAllByHolding_HoldingIdAndUser_UserId(holdingId, userId, pageable)
                .map(HoldingPositionResponse::new);
    }

    public Page<HoldingPositionResponse> getPositionsByAccount(UUID accountId, String userId, Pageable pageable) {
        return holdingPositionRepository
                .findAllByAccount_AccountIdAndUser_UserId(accountId, userId, pageable)
                .map(HoldingPositionResponse::new);
    }

    public Page<HoldingPositionResponse> getPositionsByHoldingAndStatus(
            UUID holdingId,
            String userId,
            HoldingStatus status,
            Pageable pageable
    ) {
        return holdingPositionRepository
                .findAllByHolding_HoldingIdAndUser_UserIdAndHoldingStatus(holdingId, userId, status, pageable)
                .map(HoldingPositionResponse::new);
    }

    public Page<HoldingPositionResponse> getPositionsByAccountAndStatus(
            UUID accountId,
            String userId,
            HoldingStatus status,
            Pageable pageable
    ) {
        return holdingPositionRepository
                .findAllByAccount_AccountIdAndUser_UserIdAndHoldingStatus(accountId, userId, status, pageable)
                .map(HoldingPositionResponse::new);
    }

    public HoldingPositionResponse getPosition(UUID positionId, String userId) {
        HoldingPosition position = findOwnedPosition(positionId, userId);
        return new HoldingPositionResponse(position);
    }

    @Transactional
    public HoldingPositionResponse updatePosition(UUID positionId, String userId, UpdateHoldingPositionRequest request) {
        HoldingPosition position = findOwnedPosition(positionId, userId);

        position.setPositionName(normalize(request.positionName()));
        position.setFolioNumber(normalize(request.folioNumber()));
        position.setNotes(normalize(request.notes()));

        // No save() is needed: position is a managed entity.
        // JPA dirty checking persists changes at transaction commit.
        return new HoldingPositionResponse(position);
    }

    @Transactional
    public HoldingPositionResponse updatePositionStatus(UUID positionId, String userId, HoldingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Holding status must not be null");
        }

        HoldingPosition position = findOwnedPosition(positionId, userId);
        position.setHoldingStatus(status);

        return new HoldingPositionResponse(position);
    }

    private HoldingPosition findOwnedPosition(UUID positionId, String userId) {
        return holdingPositionRepository
                .findByPositionIdAndUser_UserId(positionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding position not found"));
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
