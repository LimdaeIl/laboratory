package com.book.laboratory.user.application;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.jwt.JwtService;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.GetMyInfoResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserErrorCode;
import com.book.laboratory.user.domain.UserRepository;
import com.book.laboratory.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private void existsUserByEmail(String email) {
    if (userRepository.existsUserByEmail(email)) {
      throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
    }
  }

  private User findUserByEmail(String email) {
    return userRepository.findUserByEmail(email)
        .orElseThrow(() -> new CustomException(UserErrorCode.INVALID_LOGIN));
  }

  private User findUserById(Long id) {
    return userRepository.findUserById(id)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND_BY_ID));
  }

  private void validateAccessPermission(CustomUserDetails requester, Long targetUserId) {
    boolean isAdmin = requester.role() == UserRole.ROLE_ADMIN;
    boolean isSelf = requester.id().equals(targetUserId);

    if (!isAdmin && !isSelf) {
      throw new CustomException(UserErrorCode.USER_GET_FORBIDDEN);
    }
  }


  @Transactional
  @Override
  public SignupResponseDto signup(SignupRequestDto requestDto) {
    existsUserByEmail(requestDto.email());

    User buildUser = User.builder()
        .email(requestDto.email())
        .password(passwordEncoder.encode(requestDto.password()))
        .name(requestDto.name())
        .profileImageUrl(requestDto.profileImageUrl())
        .build();

    User saveUser = userRepository.save(buildUser);

    return SignupResponseDto.from(saveUser);
  }

  @Transactional
  @Override
  public LoginResponseDto login(LoginRequestDto requestDto) {
    User userByEmail = findUserByEmail(requestDto.email());

    if (!passwordEncoder.matches(requestDto.password(), userByEmail.getPassword())) {
      throw new CustomException(UserErrorCode.INVALID_LOGIN);
    }

    String accessToken = jwtService.generateAccessToken(userByEmail);
    String refreshToken = jwtService.generateRefreshToken(userByEmail);

    return LoginResponseDto.from(userByEmail, accessToken, refreshToken);
  }

  @Transactional(readOnly = true)
  @Override
  public GetMyInfoResponseDto getMyInfo(CustomUserDetails userDetails, Long targetUserId) {
    User targetUser = findUserById(targetUserId);

    validateAccessPermission(userDetails, targetUserId);

    return GetMyInfoResponseDto.from(targetUser);
  }


}
