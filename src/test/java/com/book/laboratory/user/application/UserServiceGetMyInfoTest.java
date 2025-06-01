package com.book.laboratory.user.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.application.dto.response.GetMyInfoResponseDto;
import com.book.laboratory.user.domain.SocialType;
import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserErrorCode;
import com.book.laboratory.user.domain.UserRepository;
import com.book.laboratory.user.domain.UserRole;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceGetMyInfoTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  private final Long userId = 1L;
  private final Long otherUserId = 2L;

  private final CustomUserDetails regularUser = new CustomUserDetails(userId, "user@example.com", UserRole.ROLE_USER);
  private final CustomUserDetails adminUser = new CustomUserDetails(99L, "admin@example.com", UserRole.ROLE_ADMIN);

  private User createUser(Long id, String email) {
    return User.builder()
        .id(id)
        .name("Test User")
        .email(email)
        .profileImageUrl("https://example.com/profile.jpg")
        .userRole(UserRole.ROLE_USER)
        .socialType(SocialType.NONE)
        .build();
  }

  @DisplayName("[getMyInfo]: 자신의 정보를 요청하면 정상반환")
  @Test
  void shouldReturnMyInfo_whenUserRequestsOwnData_ThenSuccess() {
    // given
    User user = createUser(userId, "user@example.com");
    given(userRepository.findUserById(userId)).willReturn(Optional.of(user));

    // when
    GetMyInfoResponseDto result = userService.getMyInfo(regularUser, userId);

    // then
    assertThat(result.name()).isEqualTo("Test User");
    assertThat(result.email()).isEqualTo("user@example.com");
    assertThat(result.profileImageUrl()).isEqualTo("https://example.com/profile.jpg");
    assertThat(result.userRole()).isEqualTo(UserRole.ROLE_USER);
    assertThat(result.socialType()).isEqualTo(SocialType.NONE);
  }

  @DisplayName("[getMyInfo]: 관리자가 다른 사용자의 정보를 요청하면 정상반환")
  @Test
  void shouldReturnOtherUserInfoWhenAdminRequests() {
    // given
    User otherUser = createUser(otherUserId, "other@example.com");
    given(userRepository.findUserById(otherUserId)).willReturn(Optional.of(otherUser));

    // when
    GetMyInfoResponseDto result = userService.getMyInfo(adminUser, otherUserId);

    // then
    assertThat(result.email()).isEqualTo("other@example.com");
  }

  @DisplayName("[getMyInfo]: 일반 사용자가 다른 사용자 정보를 요청하면 예외 발생")
  @Test
  void ShouldThrowExceptionWhenRegularUserRequestsOtherUserInfo() {
    // given
    User otherUser = createUser(otherUserId, "other@example.com");
//    given(userRepository.findUserById(otherUserId)).willReturn(Optional.of(otherUser));

    // when & then
    assertThatThrownBy(() -> userService.getMyInfo(regularUser, otherUserId))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining(UserErrorCode.USER_GET_FORBIDDEN.getMessage());
  }

  @DisplayName("[getMyInfo]: 존재하지 않는 사용자 요청시 예외발생")
  @Test
  void ShouldThrowExceptionWhenUserNotFound() {
    // given
//    given(userRepository.findUserById(999L)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> userService.getMyInfo(regularUser, 999L))
        .isInstanceOf(CustomException.class);
  }
}
