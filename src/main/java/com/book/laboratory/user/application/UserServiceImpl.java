package com.book.laboratory.user.application;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserErrorCode;
import com.book.laboratory.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  /**
   * Checks if a user with the specified email exists and throws an exception if found.
   *
   * @param email the email address to check for existing users
   * @throws CustomException if a user with the given email already exists
   */
  private void existsUserByEmail(String email) {
    if (userRepository.existsUserByEmail(email)) {
      throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
    }
  }

  /**
   * Registers a new user with the provided signup information.
   *
   * Checks for duplicate email addresses before creating and saving a new user entity.
   * Returns a response DTO representing the newly registered user.
   *
   * @param requestDto the signup request containing user details
   * @return a response DTO with the registered user's information
   * @throws CustomException if a user with the given email already exists
   */
  @Override
  public SignupResponseDto signup(SignupRequestDto requestDto) {
    existsUserByEmail(requestDto.email());

    User buildUser = User.builder()
        .email(requestDto.email())
        .password(requestDto.password())
        .name(requestDto.name())
        .profileImageUrl(requestDto.profileImageUrl())
        .build();

    User saveUser = userRepository.save(buildUser);

    return SignupResponseDto.from(saveUser);
  }
}
