package com.book.laboratory.user.application.user.condition;

import com.book.laboratory.user.domain.user.SocialType;
import com.book.laboratory.user.domain.user.UserRole;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record UserSearchCondition(
    Long id,
    String name,
    String email,
    UserRole userRole,
    SocialType socialType,
    String socialId,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime createdFrom,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime createdTo,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime updatedFrom,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime updatedTo,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime deletedFrom,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime deletedTo,

    Long createdBy,
    Long updatedBy,
    Long deletedBy

) {
}
