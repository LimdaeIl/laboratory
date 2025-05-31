package com.book.laboratory.user.presentation;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.UserService;
import com.book.laboratory.user.application.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.GenerateTokenResponseDto;
import com.book.laboratory.user.application.dto.response.GetMyInfoResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.dto.response.LogoutResponseDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto) {
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

    return ResponseEntity.noContent().build();
  }


}
