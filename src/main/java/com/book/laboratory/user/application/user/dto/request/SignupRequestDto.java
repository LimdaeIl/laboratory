package com.book.laboratory.user.application.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청 DTO")
public record SignupRequestDto(

    @Schema(description = "이메일", example = "test@example.com")
    @NotBlank(message = "이메일: 이메일은 필수입니다.")
    @Email(message = "이메일: 유효하지 않은 이메일 형식입니다.")
    String email,

    @Schema(description = "비밀번호", example = "P@ssw0rd!")
    @NotBlank(message = "비밀번호: 비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%?&]{8,}$",
        message = "비밀번호: 비밀번호는 최소 8자 이상이며, 영문자, 숫자, 특수문자(@, $, !, %, ?, &)를 포함해야 합니다.")
    String password,

    @Schema(description = "이름", example = "외향적인새우초밥")
    @NotBlank(message = "이름: 이름은 필수입니다.")
    @Size(max = 10, message = "이름: 이름은 10자 이하입니다.") // 어느 조건이 틀렸는지 파악하기 용이!
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9]+$",
        message = "이름: 한글, 영어 대소문자, 숫자만 입력 가능합니다."
    )
    String name,

    @Schema(description = "프로필사진", example = "https://aws.some_where/my_profile_image.png")
    String profileImageUrl
) {
}
