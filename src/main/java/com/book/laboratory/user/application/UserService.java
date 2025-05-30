package com.book.laboratory.user.application;

import com.book.laboratory.user.application.dto.request.LoginRequestDto;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.LoginResponseDto;
import com.book.laboratory.user.application.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import jakarta.validation.Valid;

public interface UserService {
  SignupResponseDto signup(SignupRequestDto requestDto);

  LoginResponseWithCookieDto login(LoginRequestDto requestDto);
}
