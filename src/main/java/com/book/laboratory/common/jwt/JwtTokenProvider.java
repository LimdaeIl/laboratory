package com.book.laboratory.common.jwt;


import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.user.domain.UserErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;

  @Value("${jwt.access.expiration}")
  private long expiration;

  public JwtTokenProvider(@Value("${jwt.secret}") String base64Secret) {
    byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(String subject, Map<String, Object> claims, long ttlMillis) {
    long now = System.currentTimeMillis();
    Date nowDate = new Date(now);
    Date expDate = new Date(now + ttlMillis);

    return Jwts.builder()
        .subject(subject)
        .issuedAt(nowDate)
        .expiration(expDate)
        .claims(claims)
        .signWith(secretKey)
        .compact();
  }

  public long getRemainingMillisByToken(String bearerToken) {
    Claims claims = extractClaims(bearerToken);
    return claims.getExpiration().getTime() - System.currentTimeMillis();
  }


  public String getTokenId(String bearerToken) {
    Claims claims = extractClaims(bearerToken);
    return claims.getId();
  }


  public Long getUserIdByToken(String bearerToken) {
    Claims claims = extractClaims(bearerToken);
    return claims.get("userId", Long.class);
  }


  public Claims extractClaims(String bearerToken) {
    if (bearerToken.isBlank() || !bearerToken.startsWith("Bearer ")) {
      throw new CustomException(UserErrorCode.INVALID_BEARER_TOKEN);
    }

    String token = bearerToken.substring(7);

    try {
      return Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      throw new CustomException(UserErrorCode.TOKEN_EXPIRED);
    } catch (MalformedJwtException e) {
      throw new CustomException(UserErrorCode.MALFORMED_TOKEN);
    } catch (SignatureException e) {
      throw new CustomException(UserErrorCode.TAMPERED_TOKEN);
    } catch (PrematureJwtException e) {
      throw new CustomException(UserErrorCode.TOKEN_TOO_EARLY);
    } catch (JwtException e) {
      throw new CustomException(UserErrorCode.INVALID_BEARER_TOKEN);
    }
  }

}
