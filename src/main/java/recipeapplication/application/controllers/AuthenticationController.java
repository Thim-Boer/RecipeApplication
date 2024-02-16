package recipeapplication.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import recipeapplication.application.exceptions.RecipeErrorCodes;
import recipeapplication.application.dto.SignInRequest;
import recipeapplication.application.dto.SignUpRequest;
import recipeapplication.application.services.AuthenticationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> Signup(@RequestBody SignUpRequest request) {
        var register = ResponseEntity.ok(authenticationService.register(request));
        if (register.getBody().getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(register.getBody());
        }
        return ResponseEntity.badRequest().body(RecipeErrorCodes.DataAlreadyExists);
    }
    
    @PostMapping("/signin") 
    public  ResponseEntity<?> Signin(@RequestBody SignInRequest request) {
        var result =  ResponseEntity.ok(authenticationService.Authenticate(request));
        if(result.getBody() == null) {
            return ResponseEntity.badRequest().body("Verkeerde gegevens");
        } else {
            return result;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> Logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok("Logout successful");
    }

    @PostConstruct
    public void ExecuteOnStartup() {
        this.authenticationService.register(new SignUpRequest("admin", "admin", "admin@admin.com", "password"));
        this.authenticationService.register(new SignUpRequest("user", "user", "user@user.com", "password"));
    }
}
