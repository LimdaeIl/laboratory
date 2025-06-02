package com.book.laboratory.user.application.user.dto.response;

import com.book.laboratory.user.domain.user.User;
import lombok.Builder;

@Builder
public record UpdateUserEmailResponseDto(
    Long id,
    String email
) {

  public static UpdateUserEmailResponseDto from(User user) {
    return UpdateUserEmailResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .build();
  }
}
