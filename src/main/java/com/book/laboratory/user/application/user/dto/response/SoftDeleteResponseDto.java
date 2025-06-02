package com.book.laboratory.user.application.user.dto.response;

import com.book.laboratory.user.domain.user.User;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SoftDeleteResponseDto(
    Long id,
    LocalDateTime deletedAt,
    Long deletedBy
) {

  public static SoftDeleteResponseDto from(User user) {
    return SoftDeleteResponseDto.builder()
        .id(user.getId())
        .deletedAt(LocalDateTime.now())
        .deletedBy(user.getId())
        .build();
  }

}
