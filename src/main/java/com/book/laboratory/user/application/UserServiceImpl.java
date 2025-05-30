package com.book.laboratory.user.application;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.jwt.JwtService;
import com.book.laboratory.common.redis.RedisKeySupport;
import com.book.laboratory.common.redis.RedisService;
import com.book.laboratory.user.application.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserErrorCode;
import com.book.laboratory.user.domain.UserRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final RedisService redisService;

  private void existsUserByEmail(String email) {
    if (userRepository.existsUserByEmail(email)) {
      throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
    }
  }

  public User findUserByEmail(String email) {
    return userRepository.findUserByEmail(email)
        .orElseThrow(() -> new CustomException(UserErrorCode.INVALID_LOGIN));
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
  public LoginResponseWithCookieDto login(LoginRequestDto requestDto) {
    User userByEmail = findUserByEmail(requestDto.email());

    if (!passwordEncoder.matches(requestDto.password(), userByEmail.getPassword())) {
      throw new CustomException(UserErrorCode.INVALID_LOGIN);
    }

    String accessToken = jwtService.generateAccessToken(userByEmail);
    String refreshToken = jwtService.generateRefreshToken(userByEmail);

    String jtiByRT = jwtService.getTokenId(refreshToken);
    Duration ttlByRT = Duration.ofMillis(jwtService.getRefreshTokenTtl());
    String keyByRT = RedisKeySupport.refreshToken(jtiByRT);

    redisService.set(keyByRT, refreshToken, ttlByRT);

    ResponseCookie refreshCookie = ResponseCookie.from("RT", jtiByRT)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path("/")
        .maxAge(Duration.ofMillis(jwtService.getRefreshTokenTtl()))
        .build();

    return new LoginResponseWithCookieDto(
        LoginResponseDto.from(userByEmail),
        refreshCookie,
        accessToken
    );
  }


}
