package recipeapplication.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipeapplication.application.dto.AuthenticationResponse;
import recipeapplication.application.dto.SignInRequest;
import recipeapplication.application.dto.SignUpRequest;
import recipeapplication.application.exceptions.EntityIsNotUniqueException;
import recipeapplication.application.exceptions.RecordNotFoundException;
import recipeapplication.application.exceptions.WrongLoginDetailsException;
import recipeapplication.application.models.Role;
import recipeapplication.application.models.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final recipeapplication.application.repository.UserRepository repository;
    private final PasswordEncoder passwordEncoder;
  private final TokenService jwtService;
  private final AuthenticationManager authenticationManager;
  public User register(SignUpRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail().toLowerCase())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    if(repository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
      throw new EntityIsNotUniqueException("Er bestaat al een account met dit emailadres");
    }
    return repository.save(user);
  }

  public AuthenticationResponse authenticate(SignInRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getEmail().toLowerCase(),
              request.getPassword()
          )
      );
    } catch (AuthenticationException ex) {
      throw new WrongLoginDetailsException("Verkeerde gegevens");
    }
    var user = repository.findByEmail(request.getEmail().toLowerCase())
        .orElseThrow();
    var accessToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .build();
  }

  public void makeUserAdmin(Integer id) {
    User user = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Er is geen gebruiker gevonden met deze identifier: " + id));
    user.setRole(Role.ADMIN);
    repository.save(user);
  }
}