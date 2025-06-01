package com.book.laboratory.user.application.dto.request;

import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserRole;
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
