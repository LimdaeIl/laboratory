package com.book.laboratory.user.domain.user;

import com.book.laboratory.user.application.user.condition.UserSearchCondition;
import com.book.laboratory.user.application.user.dto.response.GetUsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryRepository {
  Page<GetUsersResponseDto> findUsersByCondition(UserSearchCondition condition, Pageable pageable);

}
