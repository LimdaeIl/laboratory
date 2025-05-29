package com.book.laboratory.user.application.dto.response;

import com.book.laboratory.user.domain.User;
import lombok.Builder;

@Builder
public record SignupResponseDto(
    String email,
    String name,
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
