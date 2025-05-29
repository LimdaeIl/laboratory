package com.book.laboratory.common.jwt;


import com.book.laboratory.user.domain.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
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

  /**
   * Constructs a JwtTokenProvider by decoding the provided Base64-encoded secret and initializing the signing key.
   *
   * @param base64Secret the Base64-encoded secret string used to generate the HMAC SHA signing key
   */
  public JwtTokenProvider(@Value("${jwt.secret}") String base64Secret) {
    byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Generates a JWT token containing the user's email as the subject and their role as a claim.
   *
   * @param email the user's email to set as the token subject
   * @param role the user's role to include as a claim named "ROLE"
   * @return a signed JWT token string with subject, role, issued-at, and expiration claims
   */
  public String createToken(String email, UserRole role) {
    return Jwts.builder()
        .subject(email)
        .claim("ROLE", role.toString())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000L))
        .signWith(secretKey)
        .compact();
  }
}
