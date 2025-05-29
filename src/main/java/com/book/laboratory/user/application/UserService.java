package com.book.laboratory.user.application;

import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;

public interface UserService {
  /****
 * Registers a new user with the provided signup information.
 *
 * @param requestDto the data required to create a new user account
 * @return the result of the signup operation, including user details or status
 */
SignupResponseDto signup(SignupRequestDto requestDto);
}
