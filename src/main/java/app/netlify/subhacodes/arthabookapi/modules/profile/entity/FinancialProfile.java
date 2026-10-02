package app.netlify.subhacodes.arthabookapi.modules.profile.entity;

import app.netlify.subhacodes.arthabookapi.modules.profile.enums.AnnualIncomeRange;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.Occupation;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.RiskAppetite;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "financial_profiles", schema = "profile" )
@DynamicUpdate
public class FinancialProfile {
    @Id
    @Column(name = "user_id", nullable = false, length = 128)
    private String userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "occupation", columnDefinition = "occupation" )
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    @Column(name = "annual_income_range", columnDefinition = "annual_income_range" )
    private AnnualIncomeRange annualIncomeRange;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "risk_appetite", columnDefinition = "risk_appetite" )
    private RiskAppetite riskAppetite;

    @Column(name = "base_currency", length = 3, nullable = false)
    @ColumnDefault("'INR'" )
    private String baseCurrency;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "fk_financial_profiles_user" )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public FinancialProfile() {
    }

    public FinancialProfile(User user) {
        this.user = user;
        this.userId = user.getUserId();
        this.baseCurrency = "INR";
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

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getUserId();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
