package app.netlify.subhacodes.arthabookapi.modules.profile.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceAlreadyExistsException;
import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.CreateUserRequest;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.UpdateUserRequest;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.response.FinancialProfileResponse;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.response.UserProfileResponse;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import app.netlify.subhacodes.arthabookapi.modules.profile.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserProfileResponse createProfile(String firebaseUid, String email, CreateUserRequest request) {
        if (userRepository.existsById(firebaseUid)) {
            throw new ResourceAlreadyExistsException("Profile already exists" );
        }

        User user = new User();
        user.setUserId(firebaseUid);
        user.setEmail(email);
        user.setFullName(request.fullName());
        user.setIsdCode(request.isdCode());
        user.setMobileNumber(request.mobileNumber());
        user.setProfilePhotoUrl(request.profilePhotoUrl());
        user.setDateOfBirth(request.dateOfBirth());
        user.setGender(request.gender());

        User savedUser = userRepository.save(user);
        return new UserProfileResponse(savedUser, null);
    }

    @Transactional
    public UserProfileResponse updateProfile(String firebaseUid, UpdateUserRequest request) {
        User user = findProfileWithFinancialProfile(firebaseUid);

        // Partial update – only set fields that are present (not null)
        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }
        if (request.isdCode() != null) {
            user.setIsdCode(request.isdCode());
        }
        if (request.mobileNumber() != null) {
            user.setMobileNumber(request.mobileNumber());
        }
        if (request.profilePhotoUrl() != null) {
            user.setProfilePhotoUrl(request.profilePhotoUrl());
        }
        if (request.dateOfBirth() != null) {
            user.setDateOfBirth(request.dateOfBirth());
        }
        if (request.gender() != null) {
            user.setGender(request.gender());
        }

        User savedUser = userRepository.save(user);
        return toProfileResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String firebaseUid) {
        User user = findProfileWithFinancialProfile(firebaseUid);
        return toProfileResponse(user);
    }

    @Transactional
    public String deleteProfile(String firebaseUid) {
        if (!userRepository.existsById(firebaseUid)) {
            throw new ResourceNotFoundException("User profile not found" );
        }
        userRepository.deleteById(firebaseUid);
        return firebaseUid;
    }

    // Helper methods
    private User findProfileWithFinancialProfile(String firebaseUid) {
        return userRepository.findByIdWithFinancialProfile(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found" ));
    }

    private UserProfileResponse toProfileResponse(User user) {
        FinancialProfileResponse financialProfile = user.getFinancialProfile() != null
                ? new FinancialProfileResponse(user.getFinancialProfile())
                : null;

        return new UserProfileResponse(user, financialProfile);
    }
}
