package com.book.laboratory.user.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.user.UserServiceImpl;
import com.book.laboratory.user.application.user.dto.response.SoftDeleteResponseDto;
import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserErrorCode;
import com.book.laboratory.user.domain.user.UserRepository;
import com.book.laboratory.user.domain.user.UserRole;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceSoftDeleteTest {
  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  private User user;
  private User admin;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .id(1L)
        .name("홍길동")
        .email("user@example.com")
        .userRole(UserRole.ROLE_USER)
        .build();

    admin = User.builder()
        .id(2L)
        .name("관리자")
        .email("admin@example.com")
        .userRole(UserRole.ROLE_ADMIN)
        .build();
  }

  @DisplayName("[softDeleteUser]: 일반 사용자가 자신의 계정을 삭제하면 삭제 처리됨")
  @Test
  void givenUserDeletingOwnAccount_whenSoftDelete_thenUserIsMarkedDeletedAndDtoReturned() {
    // Given
    CustomUserDetails userDetails = new CustomUserDetails(1L, "user@example.com", UserRole.ROLE_USER);
    given(userRepository.findUserById(1L)).willReturn(Optional.of(user));

    // When
    SoftDeleteResponseDto result = userService.softDeleteUser(userDetails, 1L);

    // Then
    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.deletedBy()).isEqualTo(1L);
    assertThat(user.getDeletedAt()).isNotNull();
    assertThat(user.getDeletedBy()).isEqualTo(1L);
  }

  @DisplayName("[softDeleteUser]: 관리자가 다른 사용자를 삭제하면 삭제 처리됨")
  @Test
  void givenAdminDeletingAnotherUser_whenSoftDelete_thenTargetUserIsMarkedDeleted() {
    // Given
    CustomUserDetails adminDetails = new CustomUserDetails(2L, "admin@example.com", UserRole.ROLE_ADMIN);
    given(userRepository.findUserById(1L)).willReturn(Optional.of(user));

    // When
    SoftDeleteResponseDto result = userService.softDeleteUser(adminDetails, 1L);

    // Then
    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.deletedBy()).isEqualTo(1L);
    assertThat(user.getDeletedAt()).isNotNull();
    assertThat(user.getDeletedBy()).isEqualTo(1L); // 삭제 대상 ID 기준
  }

  @DisplayName("[softDeleteUser]: 일반 사용자가 다른 사용자를 삭제하려 하면 예외 발생")
  @Test
  void givenUserDeletingOtherUser_whenSoftDelete_thenThrowsInvalidPatchUserException() {
    // Given
    CustomUserDetails userDetails = new CustomUserDetails(3L, "other@example.com", UserRole.ROLE_USER);

    // When & Then
    assertThatThrownBy(() -> userService.softDeleteUser(userDetails, 1L))
        .isInstanceOf(CustomException.class)
        .hasMessage(UserErrorCode.INVALID_PATCH_USER.getMessage());
  }

  @DisplayName("[softDeleteUser]: 존재하지 않는 사용자를 삭제 요청 시 예외 발생")
  @Test
  void givenUserNotFound_whenSoftDelete_thenThrowsUserNotFoundException() {
    // Given
    CustomUserDetails adminDetails = new CustomUserDetails(2L, "admin@example.com", UserRole.ROLE_ADMIN);
    given(userRepository.findUserById(999L)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> userService.softDeleteUser(adminDetails, 999L))
        .isInstanceOf(CustomException.class)
        .hasMessage(UserErrorCode.USER_NOT_FOUND_BY_ID.getMessage());
  }
}
