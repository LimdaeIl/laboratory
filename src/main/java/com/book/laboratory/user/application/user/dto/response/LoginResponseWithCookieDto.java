package com.book.laboratory.user.application.user.dto.response;

import org.springframework.http.ResponseCookie;

public record LoginResponseWithCookieDto(
    LoginResponseDto responseDto,
    ResponseCookie refreshCookie,
    String accessToken
    ) {
}
