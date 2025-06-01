package com.book.laboratory.user.domain;

import com.book.laboratory.user.application.dto.condition.UserSearchCondition;
import com.book.laboratory.user.application.dto.response.GetUsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryRepository {
  Page<GetUsersResponseDto> findUsersByCondition(UserSearchCondition condition, Pageable pageable);

}
