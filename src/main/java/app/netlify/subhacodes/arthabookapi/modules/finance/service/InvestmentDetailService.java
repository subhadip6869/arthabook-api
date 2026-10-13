package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.InvestmentDetailResponse;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.UpsertInvestmentDetailRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.InvestmentDetail;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.HoldingPositionRepository;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.InvestmentDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<InvestmentDetailResponse> getInvestmentDetails(String userId, Pageable pageable) {
        return investmentDetailRepository.findAllByPosition_User_UserId(userId, pageable)
                .map(InvestmentDetailResponse::new);
    }

    public InvestmentDetailResponse getInvestmentDetail(UUID positionId, String userId) {
        InvestmentDetail detail = investmentDetailRepository
                .findByPosition_PositionIdAndPosition_User_UserId(positionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Investment detail not found for position: " + positionId));

        return new InvestmentDetailResponse(detail);
    }

    @Transactional
    public InvestmentDetailResponse upsertInvestmentDetail(
            UUID positionId,
            String userId,
            UpsertInvestmentDetailRequest request) {

        HoldingPosition position = holdingPositionRepository
                .findByPositionIdAndUser_UserId(positionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding position not found with id: " + positionId));

        InvestmentDetail detail = investmentDetailRepository
                .findByPosition_PositionIdAndPosition_User_UserId(positionId, userId)
                .orElseGet(() -> {
                    InvestmentDetail newDetail = new InvestmentDetail();
                    newDetail.setPosition(position);
                    position.setInvestmentDetail(newDetail);
                    return newDetail;
                });

        detail.setIssuerName(normalize(request.issuerName()));
        detail.setInstrumentCode(normalize(request.instrumentCode()));
        detail.setIsin(normalize(request.isin()));
        detail.setExchange(normalize(request.exchange()));
        detail.setInterestRate(request.interestRate());
        detail.setInvestmentDate(request.investmentDate());
        detail.setMaturityDate(request.maturityDate());
        detail.setMaturityAmount(request.maturityAmount());

        InvestmentDetail savedDetail = investmentDetailRepository.save(detail);
        return new InvestmentDetailResponse(savedDetail);
    }

    @Transactional
    public void deleteInvestmentDetail(UUID positionId, String userId) {
        InvestmentDetail detail = investmentDetailRepository
                .findByPosition_PositionIdAndPosition_User_UserId(positionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Investment detail not found for position: " + positionId));

        HoldingPosition position = detail.getPosition();
        if (position != null) {
            position.setInvestmentDetail(null);
        }
        investmentDetailRepository.delete(detail);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
