package recipeapplication.application.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import recipeapplication.application.models.Role;
import recipeapplication.application.repository.TokenRepository;
import recipeapplication.application.services.TokenService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest
                                          request,
                                  @NonNull HttpServletResponse
                                          response,
                                  @NonNull FilterChain
                                          filterChain) throws ServletException, IOException {

    final String authorizationHeader =
            request.getHeader("Authorization");
    String username = null;
    String jwt = null;
    if (authorizationHeader != null &&
            authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      username = tokenService.extractUsername(jwt);
    }
    if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails =
              this.userDetailsService.loadUserByUsername(username);
      if (tokenService.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken
                usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities()
        );
        usernamePasswordAuthenticationToken.setDetails(new
                WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}