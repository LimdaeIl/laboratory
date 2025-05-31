package com.book.laboratory.user.application;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.jwt.JwtService;
import com.book.laboratory.common.redis.RedisKeySupport;
import com.book.laboratory.common.redis.RedisService;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.GenerateTokenResponseDto;
import com.book.laboratory.user.application.dto.response.GetMyInfoResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.dto.response.LogoutResponseDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserErrorCode;
import com.book.laboratory.user.domain.UserRepository;
import com.book.laboratory.user.domain.UserRole;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "UserServiceImpl")
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

  @Transactional(readOnly = true)
  @Override
  public GetMyInfoResponseDto getMyInfo(CustomUserDetails userDetails, Long targetUserId) {
    validateAccessPermission(userDetails, targetUserId);
    User targetUser = findUserById(targetUserId);

    return GetMyInfoResponseDto.from(targetUser);
  }

  @Transactional
  @Override
  public GenerateTokenResponseDto generateToken(String jti) {
    if (jti == null || jti.isBlank()) {
      throw new CustomException(UserErrorCode.MISSING_JWT_ID);
    }
    if (redisService.exists(RedisKeySupport.BlacklistToken(jti))) {
      throw new CustomException(UserErrorCode.TOKEN_ALREADY_USED);
    }
    String refreshTokenKey = RedisKeySupport.refreshToken(jti);
    String refreshToken = redisService.get(refreshTokenKey, String.class);

    if (refreshToken == null) {
      throw new CustomException(UserErrorCode.TOKEN_EXPIRED);
    }

    Duration remainingTtl = Duration.ofMillis(jwtService.getRemainingMillisByToken(refreshToken));
    redisService.delete(refreshTokenKey);
    redisService.set(RedisKeySupport.BlacklistToken(jti), "BL", remainingTtl);

    Long userId = jwtService.getUserId(refreshToken);
    User user = findUserById(userId);

    String newAccessToken = jwtService.generateAccessToken(user);
    String newRefreshToken = jwtService.generateRefreshToken(user);
    String newJti = jwtService.getTokenId(newRefreshToken);

    Duration newTtl = Duration.ofMillis(jwtService.getRefreshTokenTtl());
    redisService.set(RedisKeySupport.refreshToken(newJti), newRefreshToken, newTtl);

    ResponseCookie refreshCookie = ResponseCookie.from("RT", newJti)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path("/")
        .maxAge(newTtl)
        .build();

    return GenerateTokenResponseDto.of(refreshCookie, newAccessToken);
  }

  @Transactional
  @Override
  public LogoutResponseDto logout(String jti) {
    if (jti == null || jti.isBlank()) {
      throw new CustomException(UserErrorCode.MISSING_JWT_ID);
    }
    if (redisService.exists(RedisKeySupport.BlacklistToken(jti))) {
      throw new CustomException(UserErrorCode.TOKEN_ALREADY_USED);
    }

    String refreshTokenKey = RedisKeySupport.refreshToken(jti);
    String refreshToken = redisService.get(refreshTokenKey, String.class);

    Duration remainingTtl = Duration.ofMillis(jwtService.getRemainingMillisByToken(refreshToken));
    redisService.delete(refreshTokenKey);
    redisService.set(RedisKeySupport.BlacklistToken(jti), "BL", remainingTtl);

    Long userId = jwtService.getUserId(refreshToken);
    findUserById(userId);

    ResponseCookie refreshCookie = ResponseCookie.from("RT")
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path("/")
        .maxAge(0)
        .build();

    return new LogoutResponseDto(refreshCookie);
  }
}
