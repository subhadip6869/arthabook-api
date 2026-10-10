package app.netlify.subhacodes.arthabookapi.modules.profile.entity;

import app.netlify.subhacodes.arthabookapi.modules.profile.enums.AnnualIncomeRange;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.Occupation;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.RiskAppetite;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Entity
@Table(name = "financial_profiles", schema = "profile")
@DynamicUpdate
public class FinancialProfile {
    @Id
    @Column(name = "user_id", nullable = false, length = 128)
    private String userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "occupation", columnDefinition = "profile.occupation")
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "annual_income_range", columnDefinition = "profile.annual_income_range")
    private AnnualIncomeRange annualIncomeRange;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "risk_appetite", columnDefinition = "profile.risk_appetite")
    private RiskAppetite riskAppetite;

    @Column(name = "base_currency", length = 3, nullable = false)
    @ColumnDefault("'INR'")
    private String baseCurrency = "INR";

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_financial_profiles_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public FinancialProfile() {
    }

    public FinancialProfile(User user) {
        setUser(user);
    }

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = (user != null) ? user.getUserId() : null;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public AnnualIncomeRange getAnnualIncomeRange() {
        return annualIncomeRange;
    }

    public void setAnnualIncomeRange(AnnualIncomeRange annualIncomeRange) {
        this.annualIncomeRange = annualIncomeRange;
    }

    public RiskAppetite getRiskAppetite() {
        return riskAppetite;
    }

    public void setRiskAppetite(RiskAppetite riskAppetite) {
        this.riskAppetite = riskAppetite;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialProfile that = (FinancialProfile) o;
        return userId != null && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
