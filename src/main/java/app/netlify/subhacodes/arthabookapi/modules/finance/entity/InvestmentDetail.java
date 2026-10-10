package app.netlify.subhacodes.arthabookapi.modules.finance.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "investment_details",
        schema = "finance",
        check = {
                @CheckConstraint(name = "ck_investment_details_interest_rate", constraint = "interest_rate IS NULL OR interest_rate >= 0"),
                @CheckConstraint(name = "ck_investment_details_maturity_amount", constraint = "maturity_amount IS NULL OR maturity_amount >= 0"),
                @CheckConstraint(name = "ck_investment_details_dates", constraint = "investment_date IS NULL OR maturity_date IS NULL OR maturity_date >= investment_date)")
        }
)
@DynamicUpdate
public class InvestmentDetail {
    @Id
    @Column(name = "position_id", nullable = false, updatable = false)
    private UUID positionId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(
            name = "position_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_investment_details_position"))
    private HoldingPosition position;

    @Column(name = "issuer_name", length = 150)
    private String issuerName;

    @Column(name = "instrument_code", length = 100)
    private String instrumentCode;

    @Column(name = "isin", length = 12)
    private String isin;

    @Column(name = "exchange", length = 50)
    private String exchange;

    @Column(name = "interest_rate", precision = 7, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "investment_date")
    private LocalDate investmentDate;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "maturity_amount", precision = 20, scale = 4)
    private BigDecimal maturityAmount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    public UUID getPositionId() {
        return positionId;
    }

    public HoldingPosition getPosition() {
        return position;
    }

    public void setPosition(HoldingPosition position) {
        this.position = position;
        this.positionId = position != null
                ? position.getPositionId()
                : null;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode) {
        this.instrumentCode = instrumentCode;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(LocalDate investmentDate) {
        this.investmentDate = investmentDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public BigDecimal getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(BigDecimal maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }
}
