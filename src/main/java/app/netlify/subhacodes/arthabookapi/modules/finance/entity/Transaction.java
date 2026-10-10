package app.netlify.subhacodes.arthabookapi.modules.finance.entity;


import app.netlify.subhacodes.arthabookapi.modules.finance.enums.TransactionType;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "transactions",
        schema = "finance",
        indexes = {
                @Index(name = "idx_transactions_user_date", columnList = "user_id, transaction_date DESC"),
                @Index(name = "idx_transactions_position_date", columnList = "position_id, user_id, transaction_date DESC")
        },
        check = {
                @CheckConstraint(name = "chk_transactions_amount", constraint = "total_amount >= 0"),
                @CheckConstraint(name = "chk_transactions_fees", constraint = "fees >= 0"),
                @CheckConstraint(name = "chk_transactions_taxes", constraint = "taxes >= 0"),
                @CheckConstraint(name = "chk_transactions_quantity", constraint = "quantity IS NULL OR quantity >= 0"),
                @CheckConstraint(name = "chk_transactions_unit_price", constraint = "unit_price IS NULL OR unit_price >= 0")
        }
)
@DynamicUpdate
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", nullable = false, updatable = false)
    @ColumnDefault("gen_random_uuid()")
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transactions_user_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "position_id", referencedColumnName = "position_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_transactions_position_id")
    )
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private HoldingPosition position;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "transaction_type", nullable = false,
            columnDefinition = "finance.transaction_type")
    private TransactionType transactionType;

    @Column(name = "transaction_date", nullable = false)
    private OffsetDateTime transactionDate;

    @Column(name = "quantity", precision = 30, scale = 10)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 20, scale = 8)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 20, scale = 4, nullable = false)
    @ColumnDefault("0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "fees", precision = 20, scale = 4, nullable = false)
    @ColumnDefault("0")
    private BigDecimal fees = BigDecimal.ZERO;

    @Column(name = "taxes", precision = 20, scale = 4, nullable = false)
    @ColumnDefault("0")
    private BigDecimal taxes = BigDecimal.ZERO;

    @Column(name = "currency", length = 3, nullable = false)
    @ColumnDefault("'INR'")
    private String currency = "INR";

    @Column(name = "reference_number", length = 50)
    private String referenceNumber;

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

    public Transaction() {
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HoldingPosition getPosition() {
        return position;
    }

    public void setPosition(HoldingPosition position) {
        this.position = position;
        if (position != null) {
            this.user = position.getUser();
        }
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public OffsetDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(OffsetDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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
        Transaction that = (Transaction) o;
        return transactionId != null && Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
