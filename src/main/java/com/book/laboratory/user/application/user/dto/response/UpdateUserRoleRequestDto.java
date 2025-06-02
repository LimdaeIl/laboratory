package com.book.laboratory.user.application.user.dto.response;

import com.book.laboratory.user.domain.user.UserRole;

public record UpdateUserRoleRequestDto(
    UserRole newUserRole
) {
}
