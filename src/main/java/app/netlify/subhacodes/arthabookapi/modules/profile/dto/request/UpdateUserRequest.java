package app.netlify.subhacodes.arthabookapi.modules.profile.dto.request;

import app.netlify.subhacodes.arthabookapi.modules.profile.enums.UserGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserRequest(
        @NotBlank
        @Size(max = 150)
        String fullName,

        @Size(max = 5)
        @Pattern(regexp = "^(?!\\s*$).+", message = "ISD code must not be blank")
        String isdCode,

        @Size(max = 20)
        @Pattern(regexp = "^(?!\\s*$).+", message = "must not be blank")
        String mobileNumber,

        @Size(max = 2000)
        @Pattern(regexp = "^(?!\\s*$).+", message = "Profile photo URL must not be blank")
        String profilePhotoUrl,

        LocalDate dateOfBirth,

        UserGender gender
) {
}
