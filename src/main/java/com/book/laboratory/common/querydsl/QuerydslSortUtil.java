package com.book.laboratory.common.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;

public class QuerydslSortUtil {

  private QuerydslSortUtil() {
    throw new UnsupportedOperationException("유틸리티 클래스로 인스턴스화할 수 없습니다.");
  }

  public static <T> List<OrderSpecifier<?>> toOrderSpecifiers(
      Sort sort,
      Class<T> clazz,
      String alias,
      Set<String> allowedFields
  ) {
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    PathBuilder<T> entityPath = new PathBuilder<>(clazz, alias);

    for (Sort.Order order : sort) {
      String property = order.getProperty().trim();
      if (!allowedFields.contains(property)) {
        continue;
      }

      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      orderSpecifiers.add(new OrderSpecifier<>(
          direction,
          entityPath.getComparable(property, Comparable.class)
      ));
    }

    return orderSpecifiers;
  }
}