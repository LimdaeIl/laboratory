package com.book.laboratory.user.application.dto.response;

import org.springframework.http.ResponseCookie;

public record LogoutResponseDto(
    ResponseCookie responseCookie
) {
}
