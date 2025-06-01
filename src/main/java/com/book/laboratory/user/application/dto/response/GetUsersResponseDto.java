package com.book.laboratory.user.application.dto.response;

import com.book.laboratory.user.domain.SocialType;
import com.book.laboratory.user.domain.UserRole;
import java.time.LocalDateTime;

public record GetUsersResponseDto(
    Long id,
    String name,
    String email,
    String profileImageUrl,
    UserRole userRole,
    SocialType socialType,
    String socialId,
    LocalDateTime createdAt,
    Long createdBy,
    LocalDateTime modifiedAt,
    Long modifiedBy,
    LocalDateTime deletedAt,
    Long deletedBy
) {
}
