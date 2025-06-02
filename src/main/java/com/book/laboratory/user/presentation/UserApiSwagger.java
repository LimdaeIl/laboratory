package com.book.laboratory.user.presentation;

import com.book.laboratory.user.application.dto.request.SignupRequestDto;
import com.book.laboratory.user.application.dto.response.SignupResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "V1 회원 서비스", description = "회원 관련 API (회원가입, 로그인 등)")
public interface UserApiSwagger {

  @Operation(
      summary = "회원가입",
      description = """
          <b>email(이메일, 필수)</b>: 유효한 이메일 형식이여야만 합니다. \n
          password(비밀번호, 필수): 최소 8자 이상이며, 영어 대소문자, 숫자, 특수문자(@, $, !, %, ?, &)를 포함해야 합니다. \n
          name(이름, 필수): 최대 10자 이하이며, 한글, 영어 대소문자, 숫자만 입력 가능합니다. \n
          profileImageUrl(프로필사진, 선택): 프로필 사진입니다. \n
          """,
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = "application/json",
              examples = {
                  @ExampleObject(
                      name = "정상적인 회원가입 요청",
                      summary = "정상적인 요청",
                      value = """
                          {
                            "email": "test@example.com",
                            "password": "P@ssw0rd123!",
                            "name": "외향적인새우초밥",
                            "profileImageUrl": "https://aws.some_where/my_profile_image.png"
                          }
                          """
                  ),
                  @ExampleObject(
                      name = "이메일 누락 요청",
                      summary = "email 누락된 요청",
                      value = """
                          {
                            "email": "",
                            "password": "P@ssw0rd123!",
                            "name": "새우",
                            "profileImageUrl": "https://aws.some_where/pic.png"
                          }
                          """
                  ),
                  @ExampleObject(
                      name = "비밀번호 유효성 실패 요청",
                      summary = "숫자/특수문자 없는 비밀번호",
                      value = """
                          {
                            "email": "user@example.com",
                            "password": "password",
                            "name": "새우",
                            "profileImageUrl": "https://aws.some_where/pic.png"
                          }
                          """
                  )
              }
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "회원가입 성공",
              content = @Content(
                  mediaType = "application/json",
                  examples = @ExampleObject(
                      name = "회원가입 응답 예시",
                      value = """
                          {
                            "email": "test@example.com",
                            "name": "외향적인새우초밥",
                            "profileImageUrl": "https://aws.some_where/my_profile_image.png"
                          }
                          """
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "유효성 검증 실패",
              content = @Content(
                  mediaType = "application/json",
                  examples = {
                      @ExampleObject(
                          name = "이메일 누락 오류",
                          summary = "email 빈값 오류",
                          value = """
                              {
                                "code": "400",
                                "message": "이메일: 이메일은 필수입니다.",
                                "errors": [
                                  {
                                    "field": "email",
                                    "reason": "이메일은 빈값일 수 없습니다."
                                  }
                                ]
                              }
                              """
                      ),
                      @ExampleObject(
                          name = "비밀번호 유효성 오류",
                          summary = "비밀번호 형식 오류",
                          value = """
                              {
                                "code": "400",
                                "message": "비밀번호: 비밀번호는 최소 8자 이상이며, 영문자, 숫자, 특수문자(@, $, !, %, *, ?, &)를 포함해야 합니다.",
                                "errors": [
                                  {
                                    "field": "password",
                                    "reason": "형식이 올바르지 않습니다."
                                  }
                                ]
                              }
                              """
                      )
                  }
              )
          ),
          @ApiResponse(
              responseCode = "409",
              description = "이미 가입된 이메일",
              content = @Content(
                  mediaType = "application/json",
                  examples = @ExampleObject(
                      name = "중복 이메일 응답 예시",
                      value = """
                          {
                            "code": "409",
                            "message": "이미 존재하는 이메일입니다."
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto);
}
