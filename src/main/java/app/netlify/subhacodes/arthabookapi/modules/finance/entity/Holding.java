package app.netlify.subhacodes.arthabookapi.modules.finance.entity;

import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "holdings",
        schema = "finance",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_holding_user_account_holding_name", columnNames = {"user_id", "account_id", "holding_name"})
        },
        check = {
                @CheckConstraint(name = "chk_holding_quantity_non_negative", constraint = "quantity >= 0"),
                @CheckConstraint(name = "chk_holding_average_cost_non_negative", constraint = "average_cost >= 0"),
                @CheckConstraint(name = "chk_holding_cost_basis_non_negative", constraint = "cost_basis >= 0")
        }
)
@DynamicUpdate
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "holding_id", nullable = false, updatable = false)
    @ColumnDefault("gen_random_uuid()")
    private UUID holdingId;

    @Column(name = "holding_name", length = 150, nullable = false)
    private String holdingName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "holding_type",
            columnDefinition = "finance.holding_type",
            nullable = false
    )
    private HoldingType holdingType;

    @Column(name = "quantity", nullable = false, precision = 30, scale = 10)
    @ColumnDefault("0")
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "average_cost", nullable = false, precision = 20, scale = 8)
    @ColumnDefault("0")
    private BigDecimal averageCost = BigDecimal.ZERO;

    @Column(name = "cost_basis", nullable = false, precision = 20, scale = 4)
    @ColumnDefault("0")
    private BigDecimal costBasis = BigDecimal.ZERO;

    @Column(name = "currency", length = 3, nullable = false)
    @ColumnDefault("'INR'")
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "holding_status",
            columnDefinition = "finance.holding_status",
            nullable = false
    )
    @ColumnDefault("'ACTIVE'")
    private HoldingStatus holdingStatus = HoldingStatus.ACTIVE;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_holding_user_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(
            value = {
                    @JoinColumn(
                            name = "account_id",
                            referencedColumnName = "account_id",
                            nullable = false
                    ),
                    @JoinColumn(
                            name = "user_id",
                            referencedColumnName = "user_id",
                            nullable = false
                    )
            },
            foreignKey = @ForeignKey(name = "fk_holding_account_id_user_id")
    )
    private Account account;

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
    @ColumnDefault("0")
    private Long version = 0L;

    public UUID getHoldingId() {
        return holdingId;
    }

    public String getHoldingName() {
        return holdingName;
    }

    public void setHoldingName(String holdingName) {
        this.holdingName = holdingName;
    }

    public HoldingType getHoldingType() {
        return holdingType;
    }

    public void setHoldingType(HoldingType holdingType) {
        this.holdingType = holdingType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public BigDecimal getCostBasis() {
        return costBasis;
    }

    public void setCostBasis(BigDecimal costBasis) {
        this.costBasis = costBasis;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HoldingStatus getHoldingStatus() {
        return holdingStatus;
    }

    public void setHoldingStatus(HoldingStatus holdingStatus) {
        this.holdingStatus = holdingStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.user = account != null ? account.getUser() : null;
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
