package com.book.laboratory.common.jwt;

import com.book.laboratory.user.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
@Service
public class JwtService {

  private final JwtTokenProvider jwtTokenProvider;

  @Value("${jwt.access.expiration}")
  private long accessTokenTtl;

  @Value("${jwt.refresh.expiration}")
  private long refreshTokenTtl;

  private static final String ISSUER = "book.laboratory.user";
  private static final String AUDIENCE = "book.laboratory.user.app";

  public String generateAccessToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getUserRole().name());
    claims.put("socialType", Optional.ofNullable(user.getSocialType())
        .map(Enum::name).orElse("NONE")
    );
    claims.put("jti", UUID.randomUUID().toString());
    claims.put("userId", user.getId());

    return jwtTokenProvider.createToken(user.getEmail(), claims, accessTokenTtl);
  }

  public String generateRefreshToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("jti", UUID.randomUUID().toString());
    claims.put("userId", user.getId());

    return jwtTokenProvider.createToken(user.getEmail(), claims, refreshTokenTtl);
  }

  public String getTokenId(String bearerToken) {
    return jwtTokenProvider.getTokenId(bearerToken);
  }

  public Long getUserId(String bearerToken) {
    return jwtTokenProvider.getUserIdByToken(bearerToken);
  }


}