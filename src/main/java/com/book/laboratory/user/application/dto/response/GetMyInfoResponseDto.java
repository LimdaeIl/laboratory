package com.book.laboratory.user.application.dto.response;

import com.book.laboratory.user.domain.SocialType;
import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserRole;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GetMyInfoResponseDto(
    String name,
    String email,
    String profileImageUrl,
    UserRole userRole,
    SocialType socialType
) {

  public static GetMyInfoResponseDto from(User user) {
    return GetMyInfoResponseDto.builder()
        .name(user.getName())
        .email(user.getEmail())
        .profileImageUrl(user.getProfileImageUrl())
        .userRole(user.getUserRole())
        .socialType(user.getSocialType())
        .build();
  }
}
