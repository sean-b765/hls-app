package dev.amethyst.app.config.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import dev.amethyst.app.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${security.jwt.secret-key}")
  private String secretKey;
  @Value("${security.jwt.iss}")
  private String issuer;
  @Value("${security.jwt.aud}")
  private String audience;
  @Value("${security.jwt.access-token.expiration-time}")
  private long accessTokenExpirationMs;
  @Value("${security.jwt.refresh-token.expiration-time}")
  private long refreshTokenExpirationMs;
  // Short-lived token for accessing video segments - /api/video/...
  private long hlsTokenExpirationMs = 1000 * 60;

  public String generateAccessToken(User user) {
    return this.generateAccessToken(new HashMap<>(), user);
  }

  public String generateRefreshToken(User user) {
    return this.generateRefreshToken(new HashMap<>(), user);
  }

  public String generateAccessToken(Map<String, Object> claims, User user) {
    // Ensure roles attached to claims
    claims.putIfAbsent("roles", user.getAuthorities());
    claims.putIfAbsent("username", user.getUsername());
    return this.buildToken(claims, user, this.accessTokenExpirationMs);
  }

  public String generateHlsToken(String mediaId, User user) {
    Map<String, Object> claims = new HashMap<>() {
      {
        put("media", mediaId);
      }
    };
    return this.buildToken(claims, user, this.hlsTokenExpirationMs);
  }

  public String generateRefreshToken(Map<String, Object> claims, User user) {
    return this.buildToken(claims, user, this.refreshTokenExpirationMs);
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
