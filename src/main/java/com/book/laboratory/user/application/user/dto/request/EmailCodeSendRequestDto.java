package com.book.laboratory.user.application.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailCodeSendRequestDto(
    @NotBlank(message = "이메일: 이메일은 필수입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    String email
) {
}
