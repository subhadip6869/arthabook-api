package app.netlify.subhacodes.arthabookapi.modules.finance.entity;

import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.HoldingType;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "holdings",
        schema = "finance",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_holdings_holding_user", columnNames = {"holding_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_holdings_user_type_status", columnList = "user_id, holding_type, holding_status")
        }
)
@DynamicUpdate
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "holding_id", nullable = false, updatable = false)
    @ColumnDefault("gen_random_uuid()")
    private UUID holdingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_holding_user_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "holding_name", length = 150, nullable = false)
    private String holdingName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "holding_type", nullable = false,
            columnDefinition = "finance.holding_type")
    private HoldingType holdingType;

    @Column(name = "currency", length = 3, nullable = false)
    @ColumnDefault("'INR'")
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "holding_status", nullable = false,
            columnDefinition = "finance.holding_status")
    @ColumnDefault("'ACTIVE'")
    private HoldingStatus holdingStatus = HoldingStatus.ACTIVE;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

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
    private Long version;

    public Holding() {
    }

    public UUID getHoldingId() {
        return holdingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holding holding = (Holding) o;
        return holdingId != null && Objects.equals(holdingId, holding.holdingId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
