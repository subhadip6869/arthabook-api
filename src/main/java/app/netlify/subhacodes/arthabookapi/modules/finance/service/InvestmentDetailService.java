package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceAlreadyExistsException;
import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.CreateInvestmentDetailRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.InvestmentDetailResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.UpdateInvestmentDetailRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.InvestmentDetail;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingPositionRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.InvestmentDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class InvestmentDetailService {
    private final InvestmentDetailRepository investmentDetailRepository;
    private final HoldingPositionRepository holdingPositionRepository;

    public InvestmentDetailService(
            InvestmentDetailRepository investmentDetailRepository,
            HoldingPositionRepository holdingPositionRepository) {
        this.investmentDetailRepository = investmentDetailRepository;
        this.holdingPositionRepository = holdingPositionRepository;
    }

    @Transactional
    public InvestmentDetailResponse createInvestmentDetail(UUID positionId, String userId,
                                                           CreateInvestmentDetailRequest request) {
        HoldingPosition position = findOwnedPosition(positionId, userId);

        if (investmentDetailRepository.existsById(positionId)) {
            throw new ResourceAlreadyExistsException("Investment details already exist for this position");
        }

        InvestmentDetail detail = new InvestmentDetail();
        applyFields(
                detail,
                request.issuerName(),
                request.instrumentCode(),
                request.isin(),
                request.exchange(),
                request.interestRate(),
                request.investmentDate(),
                request.maturityDate(),
                request.maturityAmount()
        );

        // Keep both sides of the one-to-one association synchronized.
        position.setInvestmentDetail(detail);
        InvestmentDetail savedDetail = investmentDetailRepository.save(detail);

        return new InvestmentDetailResponse(savedDetail);
    }

    public InvestmentDetailResponse getInvestmentDetail(UUID positionId, String userId) {
        findOwnedPosition(positionId, userId);
        InvestmentDetail detail = investmentDetailRepository
                .findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Investment details not found"));

        return new InvestmentDetailResponse(detail);
    }

    @Transactional
    public InvestmentDetailResponse updateInvestmentDetail(UUID positionId, String userId,
                                                           UpdateInvestmentDetailRequest request) {
        findOwnedPosition(positionId, userId);
        InvestmentDetail detail = investmentDetailRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Investment details not found"));

        applyFields(
                detail,
                request.issuerName(),
                request.instrumentCode(),
                request.isin(),
                request.exchange(),
                request.interestRate(),
                request.investmentDate(),
                request.maturityDate(),
                request.maturityAmount()
        );

        // The entity is managed; JPA dirty checking persists the changes.
        return new InvestmentDetailResponse(detail);
    }

    @Transactional
    public void deleteInvestmentDetail(UUID positionId, String userId) {
        HoldingPosition position = findOwnedPosition(positionId, userId);
        if (position.getInvestmentDetail() == null) {
            throw new ResourceNotFoundException("Investment details not found");
        }

        // orphanRemoval=true deletes the associated detail at flush.
        position.setInvestmentDetail(null);
    }

    // handler methods
    private HoldingPosition findOwnedPosition(UUID positionId, String userId) {
        return holdingPositionRepository
                .findByPositionIdAndUser_UserId(positionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding position not found"));
    }

    private void applyFields(InvestmentDetail detail, String issuerName, String instrumentCode, String isin,
                             String exchange, java.math.BigDecimal interestRate, LocalDate investmentDate, LocalDate maturityDate,
                             BigDecimal maturityAmount) {
        detail.setIssuerName(normalize(issuerName));
        detail.setInstrumentCode(normalize(instrumentCode));
        detail.setIsin(normalizeIsin(isin));
        detail.setExchange(normalize(exchange));
        detail.setInterestRate(interestRate);
        detail.setInvestmentDate(investmentDate);
        detail.setMaturityDate(maturityDate);
        detail.setMaturityAmount(maturityAmount);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeIsin(String isin) {
        String normalized = normalize(isin);
        return normalized == null ? null : normalized.toUpperCase(Locale.ROOT);
    }
}
