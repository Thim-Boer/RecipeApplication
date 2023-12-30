package recipeapplication.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import recipeapplication.application.dto.AuthenticationResponse;
import recipeapplication.application.dto.SignInRequest;
import recipeapplication.application.dto.SignUpRequest;
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
        return ResponseEntity.unprocessableEntity().build();
    }
    
    @PostMapping("/signin") 
    public  ResponseEntity<AuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
