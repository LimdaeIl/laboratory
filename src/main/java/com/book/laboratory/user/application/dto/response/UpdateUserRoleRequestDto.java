package com.book.laboratory.user.application.dto.response;

import com.book.laboratory.user.domain.UserRole;

public record UpdateUserRoleRequestDto(
    UserRole newUserRole
) {
}
