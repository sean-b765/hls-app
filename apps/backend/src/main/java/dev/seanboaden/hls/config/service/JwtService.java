package dev.seanboaden.hls.config.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private long jwtExpirationMs;

  private String audience = "media-client";
  private String issuer = "media-server";

  public String generateToken(User user) {
    return this.generateToken(new HashMap<>(), user);
  }

  public String generateToken(Map<String, Object> claims, User user) {
    return this.buildToken(claims, user, jwtExpirationMs);
  }

  public String extractUsername(String token) {
    Claims claims = this.extractAllClaims(token);
    return claims.get("username").toString();
  }

  public String extractSubject(String token) {
    return this.extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return this.extractClaim(token, Claims::getExpiration);
  }

  public boolean isTokenValid(String token, UserDetails user) {
    final String username = this.extractUsername(token);
    return username.equals(user.getUsername()) && !this.isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) {
    return this.extractExpiration(token).before(new Date());
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private String buildToken(Map<String, Object> claims, User user, long expirationMs) {
    // Ensure roles attached to claims
    claims.putIfAbsent("roles", user.getAuthorities());
    claims.putIfAbsent("username", user.getUsername());

    return Jwts
        .builder()
        .claims(claims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expirationMs))
        .subject(user.getId())
        .issuer(issuer)
        .audience().add(audience).and()
        .signWith(getSigningKey())
        .compact();
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts
          .parser()
          .verifyWith(getSigningKey())
          .requireIssuer(issuer)
          .requireAudience(audience)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException ex) {
      return ex.getClaims();
    }
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
