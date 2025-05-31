package com.book.laboratory.user.application;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.GenerateTokenResponseDto;
import com.book.laboratory.user.application.dto.response.GetMyInfoResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.dto.response.LogoutResponseDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import jakarta.validation.Valid;

public interface UserService {
  SignupResponseDto signup(SignupRequestDto requestDto);

  LoginResponseWithCookieDto login(LoginRequestDto requestDto);
  GetMyInfoResponseDto getMyInfo(CustomUserDetails userDetails, Long id);

  GenerateTokenResponseDto generateToken(String jti);

  LogoutResponseDto logout(String jti);
}
