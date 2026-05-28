package app.netlify.subhacodes.arthabookapi.modules.profile.dto.response;

import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.UserGender;
import app.netlify.subhacodes.arthabookapi.modules.profile.enums.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record UserProfileResponse(
        String userId,
        String email,
        String isdCode,
        String mobileNumber,
        String fullName,
        String profilePhotoUrl,
        LocalDate dateOfBirth,
        UserGender gender,
        UserStatus status,
        boolean onboardingCompleted,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public UserProfileResponse(User user) {
        this(user.getUserId(),
                user.getEmail(),
                user.getIsdCode(),
                user.getMobileNumber(),
                user.getFullName(),
                user.getProfilePhotoUrl(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getStatus(),
                user.isOnboardingCompleted(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}