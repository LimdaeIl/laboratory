package com.book.laboratory.user.application.dto.response;

import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserRole;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record LoginResponseDto(
    Long id,
    String email,
    String name,
    String profileImageUrl,
    UserRole userRole) {

  public static LoginResponseDto from(User user) {
    return LoginResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .profileImageUrl(user.getProfileImageUrl())
        .userRole(user.getUserRole())
        .build();
  }

}
