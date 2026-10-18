package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceInUseException;
import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding.CreateHoldingRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding.HoldingResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.holding.UpdateHoldingRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.Holding;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingPositionRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingRepository;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import app.netlify.subhacodes.arthabookapi.modules.profile.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class HoldingService {
    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;
    private final HoldingPositionRepository positionRepository;

    public HoldingService(
            HoldingRepository holdingRepository,
            UserRepository userRepository,
            HoldingPositionRepository positionRepository
    ) {
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
        this.positionRepository = positionRepository;
    }

    @Transactional
    public HoldingResponse createHolding(String userId, CreateHoldingRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + userId));

        Holding holding = new Holding();
        holding.setUser(user);
        holding.setHoldingName(request.holdingName().trim());
        holding.setHoldingType(request.holdingType());
        holding.setCurrency(request.currency() == null ? "INR" : request.currency());
        holding.setNotes(normalize(request.notes()));

        Holding savedHolding = holdingRepository.saveAndFlush(holding);
        return new HoldingResponse(savedHolding);
    }

    public Page<HoldingResponse> getHoldings(String userId, Pageable pageable) {
        return holdingRepository.findAllByUser_UserId(userId, pageable)
                .map(HoldingResponse::new);
    }

    public Page<HoldingResponse> getHoldingsByTypeAndStatus(
            String userId,
            HoldingType holdingType,
            HoldingStatus holdingStatus,
            Pageable pageable
    ) {
        return holdingRepository
                .findAllByUser_UserIdAndHoldingTypeAndHoldingStatus(userId, holdingType, holdingStatus, pageable)
                .map(HoldingResponse::new);
    }

    public Page<HoldingResponse> getHoldingsByType(String userId, HoldingType holdingType, Pageable pageable) {
        return holdingRepository
                .findAllByUser_UserIdAndHoldingType(userId, holdingType, pageable)
                .map(HoldingResponse::new);
    }

    public Page<HoldingResponse> getHoldingsByStatus(String userId, HoldingStatus holdingStatus, Pageable pageable) {
        return holdingRepository
                .findAllByUser_UserIdAndHoldingStatus(userId, holdingStatus, pageable)
                .map(HoldingResponse::new);
    }

    public HoldingResponse getHolding(UUID holdingId, String userId) {
        Holding holding = findOwnedHolding(holdingId, userId);
        return new HoldingResponse(holding);
    }

    @Transactional
    public HoldingResponse updateHolding(UUID holdingId, String userId, UpdateHoldingRequest request) {
        Holding holding = findOwnedHolding(holdingId, userId);

        holding.setHoldingName(request.holdingName().trim());
        holding.setHoldingType(request.holdingType());
        holding.setCurrency(request.currency());
        holding.setNotes(normalize(request.notes()));

        holdingRepository.flush();

        return new HoldingResponse(holding);
    }

    @Transactional
    public HoldingResponse updateHoldingStatus(UUID holdingId, String userId, HoldingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Holding status must not be null");
        }

        Holding holding = findOwnedHolding(holdingId, userId);
        holding.setHoldingStatus(status);
        holdingRepository.flush();

        return new HoldingResponse(holding);
    }

    @Transactional
    public void deleteHolding(UUID holdingId, String userId) {
        Holding holding = findOwnedHolding(holdingId, userId);

        if (positionRepository.existsByHolding_HoldingIdAndUser_UserId(holdingId, userId)) {
            throw new ResourceInUseException("Cannot delete holding with associated positions. Close or update its status instead.");
        }
        holdingRepository.delete(holding);
    }

    // handler methods
    private Holding findOwnedHolding(UUID holdingId, String userId) {
        return holdingRepository
                .findByHoldingIdAndUser_UserId(holdingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found with id: " + holdingId));
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
