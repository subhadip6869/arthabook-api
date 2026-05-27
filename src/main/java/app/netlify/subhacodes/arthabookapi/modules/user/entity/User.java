package app.netlify.subhacodes.arthabookapi.modules.user.entity;

import app.netlify.subhacodes.arthabookapi.common.enums.UserStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "users", schema = "core")
public class User {
    @Id
    @Column(name = "user_id", nullable = false, insertable = false, updatable = false, length = 32)
    private String userId;

    @Column(name = "email", nullable = false, length = 150)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "core.user_status")
    private UserStatus status;

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
