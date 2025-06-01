package com.book.laboratory.user.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.book.laboratory.user.application.dto.condition.UserSearchCondition;
import com.book.laboratory.user.application.dto.response.GetUsersResponseDto;
import com.book.laboratory.user.domain.SocialType;
import com.book.laboratory.user.domain.UserQueryRepository;
import com.book.laboratory.user.domain.UserRole;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class UserServiceGetUsersTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserQueryRepository userQueryRepository;


  @DisplayName("[getUsers]: 조건 기반 회원 조회가 정상 수행된다")
  @Test
  void givenSearchCondition_whenGetUsers_thenReturnsPagedUserList() {
    // Given
    UserSearchCondition condition = new UserSearchCondition(
        null, "홍길동", null,
        UserRole.ROLE_USER, null, null,
        null, null, null, null, null, null,
        null, null, null
    );

    Pageable pageable = PageRequest.of(0, 10);
    GetUsersResponseDto user1 = new GetUsersResponseDto(
        1L, "홍길동", "hong@example.com", "img.jpg",
        UserRole.ROLE_USER, SocialType.NONE, null,
        LocalDateTime.now(), 1L,
        null, null,
        null, null
    );

    Page<GetUsersResponseDto> expectedPage = new PageImpl<>(List.of(user1), pageable, 1);

    given(userQueryRepository.findUsersByCondition(condition, pageable)).willReturn(expectedPage);

    // When
    Page<GetUsersResponseDto> result = userService.getUsers(condition, pageable);

    // Then
    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getContent().get(0).name()).isEqualTo("홍길동");
    assertThat(result.getContent().get(0).email()).isEqualTo("hong@example.com");
  }

  @DisplayName("[getUsers]: 조건에 맞는 사용자가 없는 경우 빈 페이지 반환")
  @Test
  void shouldReturnEmptyResult_whenNoUserMatchesCondition() {
    // Given
    UserSearchCondition condition = new UserSearchCondition(
        null, "없는이름", null,
        null, null, null,
        null, null, null, null, null, null,
        null, null, null
    );

    Pageable pageable = PageRequest.of(0, 10);
    Page<GetUsersResponseDto> emptyPage = Page.empty(pageable);

    given(userQueryRepository.findUsersByCondition(condition, pageable)).willReturn(emptyPage);

    // When
    Page<GetUsersResponseDto> result = userService.getUsers(condition, pageable);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.isEmpty()).isTrue();
    assertThat(result.getTotalElements()).isZero();
  }

  @DisplayName("[getUsers]: 생성일 범위 조건으로 사용자 조회")
  @Test
  void shouldReturnUsers_whenCreatedAtBetweenGiven() {
    // Given
    LocalDateTime from = LocalDateTime.now().minusDays(30);
    LocalDateTime to = LocalDateTime.now();
    UserSearchCondition condition = new UserSearchCondition(
        null, null, null,
        null, null, null,
        from, to, null, null, null, null,
        null, null, null
    );

    Pageable pageable = PageRequest.of(0, 10);
    GetUsersResponseDto user1 = new GetUsersResponseDto(
        1L, "user1", "user1@example.com", null,
        UserRole.ROLE_USER, SocialType.NONE, null,
        from.plusDays(1), 1L, null, null, null, null
    );
    Page<GetUsersResponseDto> page = new PageImpl<>(List.of(user1), pageable, 1);

    given(userQueryRepository.findUsersByCondition(condition, pageable)).willReturn(page);

    // When
    Page<GetUsersResponseDto> result = userService.getUsers(condition, pageable);

    // Then
    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getContent().get(0).createdAt()).isAfter(from);
    assertThat(result.getContent().get(0).createdAt()).isBefore(to);
  }



}
