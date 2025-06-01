package com.book.laboratory.user.application.dto.response;

import com.book.laboratory.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회원가입 응답 DTO")
@Builder
public record SignupResponseDto(
    @Schema(description = "이메일", example = "test@example.com")
    String email,

    @Schema(description = "이름", example = "외향적인새우초밥")
    String name,

    @Schema(description = "프로필사진", example = "https://aws.some_where/my_profile_image.png")
    String profileImageUrl
) {

  public static SignupResponseDto from(User user) {
    return SignupResponseDto.builder()
        .email(user.getEmail())
        .name(user.getName())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }
}
