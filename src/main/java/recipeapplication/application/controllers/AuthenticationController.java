package recipeapplication.application.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import recipeapplication.application.dto.AuthenticationResponse;
import recipeapplication.application.dto.SignInRequest;
import recipeapplication.application.dto.SignUpRequest;
import recipeapplication.application.models.User;
import recipeapplication.application.services.AuthenticationService;

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
        return ResponseEntity.created(uri).body(result);
    }
    
    @PostMapping("/signin") 
    public  ResponseEntity<AuthenticationResponse> signin(@RequestBody SignInRequest request) {
            return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PutMapping("/{userId}/admin")
    public ResponseEntity<?> makeUserAdmin(@PathVariable Integer userId) {
        authenticationService.makeUserAdmin(userId);
        return ResponseEntity.ok().body("Gebruiker: " + userId + " is admin gemaakt");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful");
    }
}
