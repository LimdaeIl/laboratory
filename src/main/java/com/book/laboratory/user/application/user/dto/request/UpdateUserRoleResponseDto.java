package com.book.laboratory.user.application.user.dto.request;

import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserRole;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UpdateUserRoleResponseDto(
    Long id,
    UserRole userRole
) {

  public static UpdateUserRoleResponseDto from(User user) {
    return UpdateUserRoleResponseDto.builder()
        .id(user.getId())
        .userRole(user.getUserRole())
        .build();
  }

}
