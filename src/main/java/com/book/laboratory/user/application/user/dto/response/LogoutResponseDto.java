package com.book.laboratory.user.application.user.dto.response;

import org.springframework.http.ResponseCookie;

public record LogoutResponseDto(
    ResponseCookie responseCookie
) {
}
