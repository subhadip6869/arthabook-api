package app.netlify.subhacodes.arthabookapi.modules.profile.controller;

import app.netlify.subhacodes.arthabookapi.common.security.FirebaseUserPrincipal;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.request.UpdateFinancialProfileRequest;
import app.netlify.subhacodes.arthabookapi.modules.profile.dto.response.FinancialProfileResponse;
import app.netlify.subhacodes.arthabookapi.modules.profile.service.FinancialProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile/financial" )
public class FinancialProfileController {
    private final FinancialProfileService financialProfileService;

    public FinancialProfileController(FinancialProfileService financialProfileService) {
        this.financialProfileService = financialProfileService;
    }

    @GetMapping
    public ResponseEntity<FinancialProfileResponse> getFinancialProfile(
            @AuthenticationPrincipal FirebaseUserPrincipal principal) {

        var profile = financialProfileService.getByUserId(principal.uid());
        return ResponseEntity.ok(new FinancialProfileResponse(profile));
    }

    @PutMapping
    public ResponseEntity<FinancialProfileResponse> saveFinancialProfile(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @Valid @RequestBody UpdateFinancialProfileRequest request) {

        var profile = financialProfileService.saveFinancialProfile(
                principal.uid(),
                request
        );
        return ResponseEntity.ok(new FinancialProfileResponse(profile));
    }
}
