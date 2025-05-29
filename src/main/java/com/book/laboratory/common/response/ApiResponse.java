package com.book.laboratory.common.response;


import static com.book.laboratory.common.response.ApiSuccessCode.OK;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"code", "message", "data"})
public record ApiResponse<T>(Integer code, String message, T data) {

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(OK.getCode(), OK.getMessage(), data);
  }

  public static <T> ApiResponse<T> success() {
    return new ApiResponse<>(OK.getCode(), OK.getMessage(), null);
  }

  public static <T> ApiResponse<T> failure(Integer code, String message, T data) {
    return new ApiResponse<>(code, message, data);
  }

  public static <T> ApiResponse<T> failure(Integer code, String message) {
    return new ApiResponse<>(code, message, null);
  }
}
