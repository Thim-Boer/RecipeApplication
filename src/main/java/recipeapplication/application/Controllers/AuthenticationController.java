package recipeapplication.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import recipeapplication.application.Exceptions.RecipeErrorCodes;
import recipeapplication.application.dto.AuthenticationResponse;
import recipeapplication.application.dto.SignInRequest;
import recipeapplication.application.dto.SignUpRequest;
import recipeapplication.application.models.Role;
import recipeapplication.application.services.AuthenticationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        var register = ResponseEntity.ok(authenticationService.register(request));
        if (register.getBody().getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(register.getBody());
        }
        return ResponseEntity.badRequest().body(RecipeErrorCodes.DataAlreadyExists);
    }
    
    @PostMapping("/signin") 
    public  ResponseEntity<AuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok("Logout successful");
    }

    @PostConstruct
    public void executeOnStartup() {
        this.authenticationService.register(new SignUpRequest("admin", "admin", "admin@admin.com", "password", Role.ADMIN));
        this.authenticationService.register(new SignUpRequest("user", "user", "user@user.com", "password", Role.USER));
    }
}
