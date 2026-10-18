package app.netlify.subhacodes.arthabookapi.modules.finance.service;

import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.CreateInvestmentDetailRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.dto.investmentdetail.UpdateInvestmentDetailRequest;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.HoldingPosition;
import app.netlify.subhacodes.arthabookapi.modules.finance.entity.InvestmentDetail;
import app.netlify.subhacodes.arthabookapi.modules.finance.repository.InvestmentDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class InvestmentDetailService {
    private final InvestmentDetailRepository investmentDetailRepository;

    public InvestmentDetailService(InvestmentDetailRepository investmentDetailRepository) {
        this.investmentDetailRepository = investmentDetailRepository;
    }

    @Transactional
    public InvestmentDetail attachInvestmentDetail(HoldingPosition position, CreateInvestmentDetailRequest request) {
        if (request == null) {
            return null;
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

        detail.setPosition(position);
        position.setInvestmentDetail(detail);

        return detail;
    }

    @Transactional
    public InvestmentDetail upsertInvestmentDetail(HoldingPosition position, UpdateInvestmentDetailRequest request) {
        if (request == null) {
            return null;
        }

        InvestmentDetail detail = position.getInvestmentDetail();
        if (detail == null) {
            detail = new InvestmentDetail();
            detail.setPosition(position);
            position.setInvestmentDetail(detail);
        }

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

        return detail;
    }

    public void removeInvestmentDetail(HoldingPosition position) {
        if (position.getInvestmentDetail() != null) {
            position.getInvestmentDetail().setPosition(null);
            position.setInvestmentDetail(null);
        }
    }

    private void applyFields(InvestmentDetail detail, String issuerName, String instrumentCode, String isin,
                             String exchange, BigDecimal interestRate, LocalDate investmentDate,
                             LocalDate maturityDate, BigDecimal maturityAmount) {
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
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private String normalizeIsin(String isin) {
        String normalized = normalize(isin);
        return normalized == null ? null : normalized.toUpperCase(Locale.ROOT);
    }
}
