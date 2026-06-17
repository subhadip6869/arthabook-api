package app.netlify.subhacodes.arthabookapi.modules.profile.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceAlreadyExistsException;
import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.CreateUserRequest;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.UpdateUserRequest;
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
            throw new ResourceAlreadyExistsException("Profile already exists");
        }

        User user = new User();
        user.setUserId(firebaseUid);
        user.setEmail(email);

        populateProfileFields(user, request, null);
        return new UserProfileResponse(userRepository.save(user));
    }

    @Transactional
    public UserProfileResponse updateProfile(String firebaseUid, UpdateUserRequest request) {
        User user = userRepository.findById(firebaseUid).orElseThrow(() ->
                new ResourceNotFoundException("User profile not found"));

        populateProfileFields(user, null, request);
        return new UserProfileResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String firebaseUid) {
        User user = userRepository.findById(firebaseUid).orElseThrow(() ->
                new ResourceNotFoundException("User profile not found"));
        return new UserProfileResponse(user);
    }

    /* Helper Methods */
    private void populateProfileFields(User user, CreateUserRequest createRequest, UpdateUserRequest updateRequest) {
        if (createRequest != null) {
            user.setFullName(createRequest.fullName());
            user.setIsdCode(createRequest.isdCode());
            user.setMobileNumber(createRequest.mobileNumber());
            user.setDateOfBirth(createRequest.dateOfBirth());
            user.setGender(createRequest.gender());
            user.setProfilePhotoUrl(createRequest.profilePhotoUrl());
        } else {
            user.setFullName(updateRequest.fullName());
            user.setIsdCode(updateRequest.isdCode());
            user.setMobileNumber(updateRequest.mobileNumber());
            user.setDateOfBirth(updateRequest.dateOfBirth());
            user.setGender(updateRequest.gender());
            user.setProfilePhotoUrl(updateRequest.profilePhotoUrl());
        }
    }
}
