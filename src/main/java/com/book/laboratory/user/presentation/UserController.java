package com.book.laboratory.user.presentation;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.UserService;
import com.book.laboratory.user.application.dto.condition.UserSearchCondition;
import com.book.laboratory.user.application.dto.request.EmailCodeSendRequestDto;
import com.book.laboratory.user.application.dto.request.EmailCodeVerifyRequestDto;
import com.book.laboratory.user.application.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.request.UpdatePasswordRequestDto;
import com.book.laboratory.user.application.dto.request.UpdateUserEmailRequestDto;
import com.book.laboratory.user.application.dto.request.UpdateUserRoleResponseDto;
import com.book.laboratory.user.application.dto.request.updateUserInfoRequestDto;
import com.book.laboratory.user.application.dto.response.GenerateTokenResponseDto;
import com.book.laboratory.user.application.dto.response.GetMyInfoResponseDto;
import com.book.laboratory.user.application.dto.response.GetUsersResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.dto.response.LogoutResponseDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import com.book.laboratory.user.application.dto.response.SoftDeleteResponseDto;
import com.book.laboratory.user.application.dto.response.UpdateUserEmailResponseDto;
import com.book.laboratory.user.application.dto.response.UpdateUserInfoResponseDto;
import com.book.laboratory.user.application.dto.response.UpdateUserRoleRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController implements UserApiSwagger {

  private final UserService userService;

  @PostMapping("/signup")
  @Override
  public ResponseEntity<SignupResponseDto> signup(
      @RequestBody @Valid SignupRequestDto requestDto) {
    SignupResponseDto responseDto = userService.signup(requestDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseDto);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(
      @RequestBody @Valid LoginRequestDto requestDto,
      HttpServletResponse response) {
    LoginResponseWithCookieDto result = userService.login(requestDto);
    response.addHeader("Set-Cookie", result.refreshCookie().toString());

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.accessToken())
        .body(result.responseDto());
  }


  @PreAuthorize("hasAnyRole('ADMIN', 'STORE', 'USER')")
  @GetMapping("/{id}")
  public ResponseEntity<GetMyInfoResponseDto> getMyInfo(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id
  ) {
    GetMyInfoResponseDto responseDto = userService.getMyInfo(userDetails, id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }

  @PostMapping("/refresh")
  public ResponseEntity<Void> generateToken(
      @CookieValue(value = "RT", required = false) String jti,
      HttpServletResponse response
  ) {
    GenerateTokenResponseDto responseDto = userService.generateToken(jti);
    response.setHeader(HttpHeaders.SET_COOKIE, responseDto.refreshCookie().toString());

    return ResponseEntity
        .ok()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + responseDto.accessToken())
        .build();
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue(value = "RT", required = false) String jti,
      HttpServletResponse response
  ) {
    LogoutResponseDto responseDto = userService.logout(jti);
    ResponseCookie deleteCookie = responseDto.responseCookie();

    response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

    return ResponseEntity
        .noContent()
        .build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @GetMapping
  public ResponseEntity<Page<GetUsersResponseDto>> getUsers(
      @ParameterObject UserSearchCondition condition,
      @PageableDefault(size = 10)
      @SortDefault.SortDefaults({
          @SortDefault(sort = "createdAt", direction = Direction.DESC),
          @SortDefault(sort = "id", direction = Direction.DESC)
      })
      Pageable page
  ) {
    Page<GetUsersResponseDto> responseDtos = userService.getUsers(condition, page);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDtos);
  }

  @PostMapping("/send/email-code")
  public ResponseEntity<Void> emailCodeSend(
      @RequestBody @Valid EmailCodeSendRequestDto request
  ) {
    userService.emailCodeSend(request);

    return ResponseEntity
        .noContent()
        .build();
  }

  @PostMapping("/verify/email-code")
  public ResponseEntity<Void> verifyEmailCode(
      @RequestBody @Valid EmailCodeVerifyRequestDto request
  ) {
    userService.verifyEmailCode(request);

    return ResponseEntity
        .noContent()
        .build();
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE', 'USER')")
  @PatchMapping("/{id}/password")
  public ResponseEntity<Void> updatePassword(
      @RequestBody @Valid UpdatePasswordRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id
  ) {
    userService.updatePassword(requestDto, userDetails, id);

    return ResponseEntity
        .noContent()
        .build();
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE', 'USER')")
  @PatchMapping("/{id}/update")
  public ResponseEntity<UpdateUserInfoResponseDto> updateUserInfo(
      @RequestBody @Valid updateUserInfoRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id
  ) {
    UpdateUserInfoResponseDto responseDto = userService.updateUserInfo(requestDto, userDetails, id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  @PatchMapping("/{id}/role")
  public ResponseEntity<UpdateUserRoleResponseDto> updateUserRole(
      @RequestBody @Valid UpdateUserRoleRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id
  ) {
    UpdateUserRoleResponseDto responseDto = userService.updateUserRole(requestDto, userDetails, id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE', 'USER')")
  @PostMapping("/{id}/email")
  public ResponseEntity<UpdateUserEmailResponseDto> updateEmail(
      @RequestBody @Valid UpdateUserEmailRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id
  ) {
    UpdateUserEmailResponseDto responseDto = userService.updateEmail(requestDto, userDetails, id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE', 'USER')")
  @DeleteMapping("/{id}/delete")
  public ResponseEntity<SoftDeleteResponseDto> softDeleteUser(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id
  ) {
    SoftDeleteResponseDto softDeleteResponseDto = userService.softDeleteUser(userDetails, id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(softDeleteResponseDto);
  }
}
