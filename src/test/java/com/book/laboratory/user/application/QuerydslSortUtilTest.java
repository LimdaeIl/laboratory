package com.book.laboratory.user.application;


import static org.assertj.core.api.Assertions.assertThat;

import com.book.laboratory.common.querydsl.QuerydslSortUtil;
import com.book.laboratory.user.domain.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class QuerydslSortUtilTest {

  @DisplayName("[toOrderSpecifiers]: 허용된 필드 정렬은 생성된다")
  @Test
  void shouldGenerateOrderSpecifiers_whenAllowedFieldsGiven() {
    // Given
    Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.desc("createdAt"));
    Set<String> allowedFields = Set.of("name", "createdAt");

    // When
    List<OrderSpecifier<?>> orderSpecifiers = QuerydslSortUtil.toOrderSpecifiers(
        sort,
        User.class,
        "user",
        allowedFields
    );

    // Then
    assertThat(orderSpecifiers).hasSize(2);
    assertThat(orderSpecifiers.get(0).getOrder()).isEqualTo(Order.ASC);
    assertThat(orderSpecifiers.get(1).getOrder()).isEqualTo(Order.DESC);
  }

  @DisplayName("[toOrderSpecifiers]: 허용되지 않은 필드는 무시된다")
  @Test
  void shouldIgnoreFieldsNotInAllowedFields() {
    // Given
    Sort sort = Sort.by("name", "invalidField");
    Set<String> allowedFields = Set.of("name");

    // When
    List<OrderSpecifier<?>> orderSpecifiers = QuerydslSortUtil.toOrderSpecifiers(
        sort,
        User.class,
        "user",
        allowedFields
    );

    // Then
    assertThat(orderSpecifiers).hasSize(1); // invalidField 무시됨
  }

  @DisplayName("[toOrderSpecifiers]: 정렬 조건이 없으면 빈 리스트 반환")
  @Test
  void shouldReturnEmptyList_whenSortIsEmpty() {
    // Given
    Sort sort = Sort.unsorted();
    Set<String> allowedFields = Set.of("id", "name");

    // When
    List<OrderSpecifier<?>> orderSpecifiers = QuerydslSortUtil.toOrderSpecifiers(
        sort,
        User.class,
        "user",
        allowedFields
    );

    // Then
    assertThat(orderSpecifiers).isEmpty();
  }
}