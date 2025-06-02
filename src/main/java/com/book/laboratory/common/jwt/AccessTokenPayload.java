package com.book.laboratory.common.jwt;

import com.book.laboratory.user.domain.user.SocialType;
import com.book.laboratory.user.domain.user.UserRole;
import java.util.Date;
import lombok.Builder;

@Builder
public record AccessTokenPayload(
    String subject,       // sub: 사용자 식별자
    UserRole role,        // 사용자 권한 (커스텀 클레임)
    String issuer,        // iss: 토큰 발급자
    String audience,      // aud: 토큰 대상자
    Date issuedAt,        // iat: 발급 시각
    Date notBefore,       // nbf: 유효 시작 시각
    Date expiration,      // exp: 만료 시각
    String jwtId,          // jti: 고유 토큰 ID
    SocialType socialType  // 소셜 로그인 타입
) {
}
