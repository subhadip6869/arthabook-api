package app.netlify.subhacodes.arthabookapi.modules.profile.service;

import app.netlify.subhacodes.arthabookapi.common.exceptions.ResourceNotFoundException;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.UpdateFinancialProfileRequest;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.FinancialProfile;
import app.netlify.subhacodes.arthabookapi.modules.profile.entity.User;
import app.netlify.subhacodes.arthabookapi.modules.profile.repository.FinancialProfileRepository;
import app.netlify.subhacodes.arthabookapi.modules.profile.repository.UserRepository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinancialProfileService {
    private final FinancialProfileRepository financialProfileRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public FinancialProfileService(
            FinancialProfileRepository financialProfileRepository,
            UserRepository userRepository,
            EntityManager entityManager) {
        this.financialProfileRepository = financialProfileRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public FinancialProfile getByUserId(String userId) {
        return financialProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found" ));
    }

    @Transactional
    public FinancialProfile saveFinancialProfile(String userId, UpdateFinancialProfileRequest request) {
        // Ensure the user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found" ));

        FinancialProfile profile = financialProfileRepository.findById(userId).orElse(null);

        if (profile == null) {
            // ----- CREATE path -----
            User userRef = userRepository.getReferenceById(userId);

            profile = new FinancialProfile(userRef);
            profile.setBaseCurrency("INR" );
            applyUpdates(profile, request);

            entityManager.persist(profile);          // ← use persist, not save
            return profile;
        }

        applyUpdates(profile, request);
        return financialProfileRepository.save(profile);
    }

    // Helper methods
    private void applyUpdates(FinancialProfile profile, UpdateFinancialProfileRequest request) {
        if (request.occupation() != null) {
            profile.setOccupation(request.occupation());
        }
        if (request.annualIncomeRange() != null) {
            profile.setAnnualIncomeRange(request.annualIncomeRange());
        }
        if (request.riskAppetite() != null) {
            profile.setRiskAppetite(request.riskAppetite());
        }
        if (request.baseCurrency() != null) {
            profile.setBaseCurrency(request.baseCurrency().toUpperCase());
        }
    }
}
