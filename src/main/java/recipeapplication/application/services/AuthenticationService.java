package recipeapplication.application.services;

import lombok.RequiredArgsConstructor;
import recipeapplication.application.dto.AuthenticationResponse;
import recipeapplication.application.dto.SignInRequest;
import recipeapplication.application.dto.SignUpRequest;
import recipeapplication.application.models.Token;
import recipeapplication.application.models.TokenType;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.TokenRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final recipeapplication.application.repository.UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService jwtService;
  private final AuthenticationManager authenticationManager;
  public ResponseEntity<?> register(SignUpRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    if(repository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.unprocessableEntity().build();
    }
    var savedUser = repository.save(user);
    var accessToken = jwtService.generateToken(user);
    saveUserToken(savedUser, accessToken);
    return ResponseEntity.ok(AuthenticationResponse.builder()
        .accessToken(accessToken)
        .build());
  }

  public AuthenticationResponse authenticate(SignInRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var accessToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, accessToken);
    return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .build();
  }

  private void saveUserToken(User user, String accessToken) {
    var token = Token.builder()
        .user(user)
        .token(accessToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
}