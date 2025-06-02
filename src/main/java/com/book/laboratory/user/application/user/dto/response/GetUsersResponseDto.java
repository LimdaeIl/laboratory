package com.book.laboratory.user.application.user.dto.response;

import com.book.laboratory.user.domain.user.SocialType;
import com.book.laboratory.user.domain.user.UserRole;
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
