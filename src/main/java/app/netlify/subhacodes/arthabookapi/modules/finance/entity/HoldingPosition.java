package app.netlify.subhacodes.arthabookapi.modules.finance.entity;

import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "holding_positions",
        schema = "finance",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_positions_position_user", columnNames = {"position_id", "user_id"})
        },
        check = {
                @CheckConstraint(name = "ck_positions_quantity", constraint = "quantity >= 0"),
                @CheckConstraint(name = "ck_positions_cost_basis", constraint = "cost_basis >= 0"),
                @CheckConstraint(name = "ck_positions_average_cost", constraint = "average_cost >= 0")
        },
        indexes = {
                @Index(name = "idx_positions_user_holding", columnList = "user_id, holding_id"),
                @Index(name = "idx_positions_account_user", columnList = "account_id, user_id")
        }
)
public class HoldingPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "position_id", nullable = false, updatable = false)
    @ColumnDefault("gen_random_uuid()")
    private UUID positionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_positions_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "holding_id", referencedColumnName = "holding_id", nullable = false),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_positions_holding")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Holding holding;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(
            value = {
                    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
            },
            foreignKey = @ForeignKey(name = "fk_positions_account")
    )
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Account account;

    @Column(name = "position_name", length = 200)
    private String positionName;

    @Column(name = "folio_number", length = 100)
    private String folioNumber;

    @Column(name = "quantity", nullable = false, precision = 30, scale = 10)
    @ColumnDefault("0")
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "average_cost", nullable = false, precision = 20, scale = 8)
    @ColumnDefault("0")
    private BigDecimal averageCost = BigDecimal.ZERO;

    @Column(name = "cost_basis", nullable = false, precision = 20, scale = 4)
    @ColumnDefault("0")
    private BigDecimal costBasis = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "holding_status", nullable = false,
            columnDefinition = "finance.holding_status")
    private HoldingStatus holdingStatus = HoldingStatus.ACTIVE;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public UUID getPositionId() {
        return positionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Holding getHolding() {
        return holding;
    }

    public void setHolding(Holding holding) {
        this.holding = holding;
        if (holding != null) {
            this.user = holding.getUser();
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        if (account != null) {
            this.user = account.getUser();
        }
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getFolioNumber() {
        return folioNumber;
    }

    public void setFolioNumber(String folioNumber) {
        this.folioNumber = folioNumber;
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
