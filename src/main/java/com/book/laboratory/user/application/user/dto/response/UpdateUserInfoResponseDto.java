package com.book.laboratory.user.application.user.dto.response;

import com.book.laboratory.user.domain.user.SocialType;
import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserRole;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UpdateUserInfoResponseDto(
    String name,
    String email,
    String profileImageUrl,
    UserRole userRole,
    SocialType socialType,
    String socialId
) {

  public static UpdateUserInfoResponseDto from(User user) {
    return UpdateUserInfoResponseDto.builder()
        .name(user.getName())
        .email(user.getEmail())
        .profileImageUrl(user.getProfileImageUrl())
        .userRole(user.getUserRole())
        .socialType(user.getSocialType())
        .socialId(user.getSocialId())
        .build();
  }
}
