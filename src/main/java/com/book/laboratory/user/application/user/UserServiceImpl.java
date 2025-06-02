package com.book.laboratory.user.application.user;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.jwt.JwtService;
import com.book.laboratory.common.redis.RedisKeySupport;
import com.book.laboratory.common.redis.RedisService;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.user.condition.UserSearchCondition;
import com.book.laboratory.user.application.user.dto.request.EmailCodeSendRequestDto;
import com.book.laboratory.user.application.user.dto.request.EmailCodeVerifyRequestDto;
import com.book.laboratory.user.application.user.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.user.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.user.dto.request.UpdatePasswordRequestDto;
import com.book.laboratory.user.application.user.dto.request.UpdateUserEmailRequestDto;
import com.book.laboratory.user.application.user.dto.request.UpdateUserRoleResponseDto;
import com.book.laboratory.user.application.user.dto.request.updateUserInfoRequestDto;
import com.book.laboratory.user.application.user.dto.response.GenerateTokenResponseDto;
import com.book.laboratory.user.application.user.dto.response.GetMyInfoResponseDto;
import com.book.laboratory.user.application.user.dto.response.GetUsersResponseDto;
import com.book.laboratory.user.application.user.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.user.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.user.dto.response.LogoutResponseDto;
import com.book.laboratory.user.application.user.dto.response.SignupResponseDto;
import com.book.laboratory.user.application.user.dto.response.SoftDeleteResponseDto;
import com.book.laboratory.user.application.user.dto.response.UpdateUserEmailResponseDto;
import com.book.laboratory.user.application.user.dto.response.UpdateUserInfoResponseDto;
import com.book.laboratory.user.application.user.dto.response.UpdateUserRoleRequestDto;
import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserErrorCode;
import com.book.laboratory.user.domain.user.UserQueryRepository;
import com.book.laboratory.user.domain.user.UserRepository;
import com.book.laboratory.user.domain.user.UserRole;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "UserServiceImpl")
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserQueryRepository userQueryRepository;

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private final RedisService redisService;

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String FromEmail;


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

  private Integer generateRandomNumber() {
    Random random = new Random();
    StringBuilder randomNumber = new StringBuilder();
    for (int i = 0; i < 6; i++) {
      randomNumber.append(random.nextInt(10));
    }
    return Integer.parseInt(randomNumber.toString());
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

  @Transactional(readOnly = true)
  @Override
  public Page<GetUsersResponseDto> getUsers(UserSearchCondition condition, Pageable page) {
    return userQueryRepository.findUsersByCondition(condition, page);
  }

  @Override
  public void emailCodeSend(EmailCodeSendRequestDto request) {
    existsUserByEmail(request.email());

    // Redis 등에 저장 (TTL: 예 3분)
    String key = "EC:" + request.email();
    String value = generateRandomNumber().toString();
    Duration timeout = Duration.ofMinutes(3);

    redisService.set(key, value, timeout);

    // 이메일 전송
    SimpleMailMessage message = new SimpleMailMessage();

    message.setTo(request.email());
    message.setFrom(FromEmail);
    message.setSubject("[POKO] 이메일 인증 코드");
    message.setText("인증 코드: " + value + "\n3분 이내에 입력해 주세요.");
    mailSender.send(message);

  }

  @Override
  public void verifyEmailCode(EmailCodeVerifyRequestDto request) {
    String key = "EC:" + request.email();
    String correctCode = redisService.get(key, String.class);

    if (correctCode == null || correctCode.isBlank()) {
      throw new CustomException(UserErrorCode.FAILED_VERIFY_EMAIL);
    }

    String code = request.code();

    if (!correctCode.equals(code) || correctCode.length() != 6) {
      throw new CustomException(UserErrorCode.FAILED_VERIFY_EMAIL);
    }
  }

  @Transactional
  @Override
  public void updatePassword(UpdatePasswordRequestDto requestDto, CustomUserDetails userDetails, Long id) {
    if (!userDetails.role().isAdmin() && !userDetails.id().equals(id)) {
      throw new CustomException(UserErrorCode.INVALID_PATCH_USER);
    }

    User userById = findUserById(id);

    if (userDetails.id().equals(id)) {
      if (!passwordEncoder.matches(requestDto.password(), userById.getPassword())) {
        throw new CustomException(UserErrorCode.INVALID_PASSWORD);
      }
    }

    if (passwordEncoder.matches(requestDto.newPassword(), userById.getPassword())) {
      throw new CustomException(UserErrorCode.DUPLICATE_PATCH_PASSWORD);
    }

    String encodedNewPassword = passwordEncoder.encode(requestDto.newPassword());
    userById.updatePassword(encodedNewPassword);
  }

  @Transactional
  @Override
  public UpdateUserInfoResponseDto updateUserInfo(updateUserInfoRequestDto requestDto,
                                                  CustomUserDetails userDetails,
                                                  Long id) {
    if (!userDetails.role().isAdmin() && !userDetails.id().equals(id)) {
      throw new CustomException(UserErrorCode.INVALID_PATCH_USER);
    }

    if (requestDto.newName() == null || requestDto.newName().isBlank()) {
      throw new CustomException(UserErrorCode.INVALID_USER_NAME);
    }

    User userById = findUserById(id);

    userById.updateName(requestDto.newName());
    userById.updateProfileImageUrl(requestDto.newProfileImageUrl() != null ? requestDto.newProfileImageUrl() : null);

    return UpdateUserInfoResponseDto.from(userById);
  }

  @Transactional
  @Override
  public UpdateUserRoleResponseDto updateUserRole(UpdateUserRoleRequestDto requestDto,
                                                  CustomUserDetails userDetails,
                                                  Long id) {

    if (!userDetails.role().isAdmin() && !userDetails.id().equals(id)) {
      throw new CustomException(UserErrorCode.INVALID_PATCH_USER);
    }

    if (requestDto.newUserRole() == null) {
      throw new CustomException(UserErrorCode.INVALID_USER_ROLE);
    }

    User userById = findUserById(id);
    userById.updateUserRole(requestDto.newUserRole());

    return UpdateUserRoleResponseDto.from(userById);
  }

  @Transactional
  @Override
  public UpdateUserEmailResponseDto updateEmail(UpdateUserEmailRequestDto requestDto,
                                                CustomUserDetails userDetails,
                                                Long id) {

    if (!userDetails.role().isAdmin() && !userDetails.id().equals(id)) {
      throw new CustomException(UserErrorCode.INVALID_PATCH_USER);
    }

    if (requestDto.newEmail() == null) {
      throw new CustomException(UserErrorCode.INVALID_PATCH_EMAIL);
    }

    User userById = findUserById(id);
    userById.updateUserEmail(requestDto.newEmail());

    return UpdateUserEmailResponseDto.from(userById);
  }

  @Transactional
  @Override
  public SoftDeleteResponseDto softDeleteUser(CustomUserDetails userDetails, Long id) {
    if (!userDetails.role().isAdmin() && !userDetails.id().equals(id)) {
      throw new CustomException(UserErrorCode.INVALID_PATCH_USER);
    }

    User userById = findUserById(id);
    userById.markDeleted(userById.getId());

    return SoftDeleteResponseDto.from(userById);
  }
}
