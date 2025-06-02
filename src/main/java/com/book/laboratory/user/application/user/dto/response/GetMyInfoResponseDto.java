package com.book.laboratory.user.application.user.dto.response;

import com.book.laboratory.user.domain.user.SocialType;
import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserRole;
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
