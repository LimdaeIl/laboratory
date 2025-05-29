package com.book.laboratory.common.response;


import static com.book.laboratory.common.response.ApiSuccessCode.OK;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"code", "message", "data"})
public record ApiResponse<T>(Integer code, String message, T data) {

  /**
   * Creates a successful API response containing the provided data.
   *
   * @param data the response payload to include
   * @return an ApiResponse instance with a success code, message, and the given data
   */
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(OK.getCode(), OK.getMessage(), data);
  }

  /**
   * Creates a successful API response with no data.
   *
   * @return an ApiResponse instance with a success code, message, and null data
   */
  public static <T> ApiResponse<T> success() {
    return new ApiResponse<>(OK.getCode(), OK.getMessage(), null);
  }

  /**
   * Creates an API response representing a failure with the specified code, message, and data.
   *
   * @param code the error code to include in the response
   * @param message the error message describing the failure
   * @param data additional data related to the failure, or null if not applicable
   * @return an ApiResponse instance representing the failure
   */
  public static <T> ApiResponse<T> failure(Integer code, String message, T data) {
    return new ApiResponse<>(code, message, data);
  }

  /**
   * Creates a failure API response with the specified code and message, and no data.
   *
   * @param code the error code representing the failure
   * @param message a descriptive message explaining the failure
   * @return an ApiResponse instance with the given code and message, and null data
   */
  public static <T> ApiResponse<T> failure(Integer code, String message) {
    return new ApiResponse<>(code, message, null);
  }
}
