package app.netlify.subhacodes.arthabookapi.modules.finance.entity;

import app.netlify.subhacodes.arthabookapi.modules.finance.enums.AccountStatus;
import app.netlify.subhacodes.arthabookapi.modules.finance.enums.AccountType;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "accounts",
        schema = "finance",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_accounts_account_id_user_id", columnNames = {"account_id", "user_id"}),
        }
)
@DynamicUpdate
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id", nullable = false, updatable = false)
    @ColumnDefault("gen_random_uuid()")
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_accounts_user_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "account_name", length = 150, nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "account_type", columnDefinition = "finance.account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "provider_name", length = 150)
    private String providerName;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "currency", length = 3, nullable = false)
    @ColumnDefault("'INR'")
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "account_status", columnDefinition = "finance.account_status", nullable = false)
    @ColumnDefault("'ACTIVE'")
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

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

    public Account() {
    }

    public UUID getAccountId() {
        return accountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
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
        Account account = (Account) o;
        return accountId != null && Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
