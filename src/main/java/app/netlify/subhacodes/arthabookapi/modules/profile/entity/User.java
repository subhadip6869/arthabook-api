package app.netlify.subhacodes.arthabookapi.modules.profile.entity;

import app.netlify.subhacodes.arthabookapi.modules.profile.enums.UserGender;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.UserStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "users",
        schema = "profile",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_email", columnNames = {"email"})
        }
)
@DynamicUpdate
public class User {
    @Id
    @Column(name = "user_id", nullable = false, length = 128)
    private String userId;

    @Column(name = "email", nullable = false, updatable = false, length = 320)
    private String email;

    @Column(name = "isd_code", length = 5)
    private String isdCode;

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "profile_photo_url",
            columnDefinition = "TEXT")
    private String profilePhotoUrl;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "gender",
            columnDefinition = "profile.user_gender")
    private UserGender gender;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false,
            columnDefinition = "profile.user_status")
    @ColumnDefault("'ACTIVE'")
    private UserStatus status = UserStatus.ACTIVE;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private FinancialProfile financialProfile;

    @Column(name = "onboarding_completed", nullable = false)
    @ColumnDefault("false")
    private boolean onboardingCompleted = false;

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

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public boolean isOnboardingCompleted() {
        return onboardingCompleted;
    }

    public void setOnboardingCompleted(boolean onboardingCompleted) {
        this.onboardingCompleted = onboardingCompleted;
    }

    public FinancialProfile getFinancialProfile() {
        return financialProfile;
    }

    public void setFinancialProfile(FinancialProfile financialProfile) {
        if (this.financialProfile != null && this.financialProfile != financialProfile) {
            this.financialProfile.setUser(null);
        }

        this.financialProfile = financialProfile;
        if (financialProfile != null && financialProfile.getUser() != this) {
            financialProfile.setUser(this);
        }
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId != null && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
