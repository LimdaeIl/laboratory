package com.book.laboratory.user.presentation;

import com.book.laboratory.common.response.ApiResponse;
import com.book.laboratory.user.application.UserService;
import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  /**
   * Handles user registration requests and returns the result.
   *
   * Accepts a JSON payload with user signup details, processes the registration, and responds with the created user's information and HTTP status 201 (Created).
   *
   * @param requestDto the signup request data
   * @return a response entity containing the signup result and HTTP status 201
   */
  @PostMapping("/signup")
  public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto) {
    SignupResponseDto responseDto = userService.signup(requestDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseDto);
  }

}
