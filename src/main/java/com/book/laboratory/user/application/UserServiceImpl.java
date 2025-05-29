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

  private void existsUserByEmail(String email) {
    if (userRepository.existsUserByEmail(email)) {
      throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
    }
  }

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
