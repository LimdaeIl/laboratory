package com.book.laboratory.user.application;

import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;

public interface UserService {
  SignupResponseDto signup(SignupRequestDto requestDto);
}
