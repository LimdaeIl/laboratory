package com.book.laboratory.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequestDto(

    @NotBlank(message = "이메일: 이메일은 필수입니다.")
    @Email(message = "이메일: 유효하지 않은 이메일 형식입니다.")
    String email,

    @NotBlank(message = "비밀번호: 비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%?&]{8,}$",
        message = "비밀번호: 비밀번호는 최소 8자 이상이며, 영문자, 숫자, 특수문자(@, $, !, %, ?, &)를 포함해야 합니다.")
    String password,

    @NotBlank(message = "이름: 이름은 필수입니다.")
    String name,

    String profileImageUrl
) {
}
