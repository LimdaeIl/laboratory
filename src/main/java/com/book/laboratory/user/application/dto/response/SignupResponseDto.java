package com.book.laboratory.user.application.dto.response;

import com.book.laboratory.user.domain.User;
import lombok.Builder;

@Builder
public record SignupResponseDto(
    String email,
    String name,
    String profileImageUrl
) {

  /**
   * Creates a SignupResponseDto from the given User domain object.
   *
   * @param user the User entity to extract signup response data from
   * @return a SignupResponseDto containing the user's email, name, and profile image URL
   */
  public static SignupResponseDto from(User user) {
    return SignupResponseDto.builder()
        .email(user.getEmail())
        .name(user.getName())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }
}
