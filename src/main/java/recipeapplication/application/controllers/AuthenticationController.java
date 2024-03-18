package recipeapplication.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import recipeapplication.application.dto.SignInRequest;
import recipeapplication.application.dto.SignUpRequest;
import recipeapplication.application.services.AuthenticationService;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        var result= authenticationService.register(request);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + result.getId()).toUriString());
        return ResponseEntity.created(uri).body("Gebruiker aangemaakt");
    }
    
    @PostMapping("/signin") 
    public  ResponseEntity<?> signin(@RequestBody SignInRequest request) {
        var result =  ResponseEntity.ok(authenticationService.authenticate(request));
        if(result.getBody() == null) {
            return ResponseEntity.badRequest().body("Verkeerde gegevens");
        } else {
            return result;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok("Logout successful");
    }

    @PostConstruct
    public void ExecuteOnStartup() {
        this.authenticationService.register(new SignUpRequest("admin", "admin", "admin@admin.com", "password"));
        this.authenticationService.register(new SignUpRequest("user", "user", "user@user.com", "password"));
    }
}
