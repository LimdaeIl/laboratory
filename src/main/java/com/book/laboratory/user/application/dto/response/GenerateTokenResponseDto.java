package com.book.laboratory.user.application.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.http.ResponseCookie;

@Builder(access = AccessLevel.PRIVATE)
public record GenerateTokenResponseDto(
    ResponseCookie refreshCookie,
    String accessToken
) {
  public static GenerateTokenResponseDto of(ResponseCookie refreshCookie, String accessToken) {
    return GenerateTokenResponseDto.builder()
        .refreshCookie(refreshCookie)
        .accessToken(accessToken)
        .build();
  }
}
