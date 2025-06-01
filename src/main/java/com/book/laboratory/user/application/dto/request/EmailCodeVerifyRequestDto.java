package com.book.laboratory.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailCodeVerifyRequestDto(
    @NotBlank(message = "이메일: 이메일은 필수입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    String email,

    @NotBlank(message = "이메일: 인증 코드는 필수입니다.")
    @Pattern(regexp = "\\d{6}", message = "이메일: 인증 코드는 6자리 숫자여야 합니다.")
    String code
) {
}
