package recipeapplication.application.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

  @Value("${application.security.jwt.secret-key}")
  public String secretKey;
  @Value("${application.security.jwt.issuertoken}")
  public String issuerToken;
  @Value("${application.security.jwt.expiration}")
  public long jwtExpiration;
  @Value("${application.security.jwt.refresh-token.expiration}")
  public long refreshExpiration;

  public String ExtractUsername(String token) {
    return ExtractClaim(token, Claims::getSubject);
  }

  public <T> T ExtractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = ExtractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String GenerateToken(UserDetails userDetails) {
    return GenerateToken(new HashMap<>(), userDetails);
  }

  public String GenerateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return BuildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String GenerateRefreshToken(
      UserDetails userDetails
  ) {
    return BuildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String BuildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuer(issuerToken)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(GetSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean IsTokenValid(String token, UserDetails userDetails) {
    final String username = ExtractUsername(token);
    if(!issuerToken.equals(ExtractClaim(token, Claims::getIssuer))) {
      return false;
    }
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return ExtractClaim(token, Claims::getExpiration).before(new Date());
  }

  private Claims ExtractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(GetSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key GetSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}