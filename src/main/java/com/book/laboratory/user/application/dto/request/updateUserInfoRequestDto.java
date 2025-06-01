package com.book.laboratory.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record updateUserInfoRequestDto(

    @NotBlank(message = "이름: 이름은 필수입니다.")
    @Size(max = 10, message = "이름: 이름은 10자 이하입니다.")
    String newName,

    String newProfileImageUrl
) {
}
