package app.netlify.subhacodes.arthabookapi.modules.profile.controller;

import app.netlify.subhacodes.arthabookapi.common.security.FirebaseUserPrincipal;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.CreateUserRequest;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.UpdateUserRequest;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.response.UserProfileResponse;
import app.netlify.subhacodes.arthabookapi.modules.profile.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> createUserProfile(@AuthenticationPrincipal FirebaseUserPrincipal principal,
                                                                 @Valid @RequestBody CreateUserRequest request) {
        UserProfileResponse response = userService.createProfile(principal.uid(),
                principal.email(),
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateUserProfile(@AuthenticationPrincipal FirebaseUserPrincipal principal,
                                                                 @Valid @RequestBody UpdateUserRequest request) {
        UserProfileResponse response = userService.updateProfile(
                principal.uid(),
                request
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal FirebaseUserPrincipal principal) {
        UserProfileResponse response = userService.getProfile(principal.uid());
        return ResponseEntity.ok(response);
    }
}
