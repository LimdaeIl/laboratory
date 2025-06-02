package com.book.laboratory.user.application.user;

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
import com.book.laboratory.user.application.user.dto.response.LoginResponseWithCookieDto;
import com.book.laboratory.user.application.user.dto.response.LogoutResponseDto;
import com.book.laboratory.user.application.user.dto.response.SignupResponseDto;
import com.book.laboratory.user.application.user.dto.response.SoftDeleteResponseDto;
import com.book.laboratory.user.application.user.dto.response.UpdateUserEmailResponseDto;
import com.book.laboratory.user.application.user.dto.response.UpdateUserInfoResponseDto;
import com.book.laboratory.user.application.user.dto.response.UpdateUserRoleRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  SignupResponseDto signup(SignupRequestDto requestDto);

  LoginResponseWithCookieDto login(LoginRequestDto requestDto);

  GetMyInfoResponseDto getMyInfo(CustomUserDetails userDetails, Long id);

  GenerateTokenResponseDto generateToken(String jti);

  LogoutResponseDto logout(String jti);

  Page<GetUsersResponseDto> getUsers(UserSearchCondition condition, Pageable page);

  void emailCodeSend(EmailCodeSendRequestDto request);

  void verifyEmailCode(EmailCodeVerifyRequestDto request);

  void updatePassword(UpdatePasswordRequestDto requestDto, CustomUserDetails userDetails, Long id);

  UpdateUserInfoResponseDto updateUserInfo(updateUserInfoRequestDto requestDto, CustomUserDetails userDetails, Long id);

  UpdateUserRoleResponseDto updateUserRole(UpdateUserRoleRequestDto requestDto, CustomUserDetails userDetails, Long id);

  UpdateUserEmailResponseDto updateEmail(UpdateUserEmailRequestDto requestDto, CustomUserDetails userDetails, Long id);

  SoftDeleteResponseDto softDeleteUser(CustomUserDetails userDetails, Long id);
}
