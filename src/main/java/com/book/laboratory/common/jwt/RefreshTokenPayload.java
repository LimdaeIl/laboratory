package com.book.laboratory.common.jwt;

import java.util.Date;
import lombok.Builder;

@Builder
public record RefreshTokenPayload(
    String subject,           // sub (user 식별용)
    String issuer,
    Date issuedAt,
    Date expiration,
    String jwtId
) {
}
