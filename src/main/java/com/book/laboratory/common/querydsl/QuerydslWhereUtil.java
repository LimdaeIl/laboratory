package com.book.laboratory.common.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import java.time.LocalDateTime;
import java.util.Collection;

public class QuerydslWhereUtil {

  private QuerydslWhereUtil() {
    throw new UnsupportedOperationException("Utility Class");
  }

  public static BooleanExpression safe(BooleanExpression expression) {
    return expression;
  }

  public static BooleanExpression like(StringPath path, String keyword) {
    return (keyword == null || keyword.isBlank()) ? null : path.containsIgnoreCase(keyword.trim());
  }

  // eq
  public static BooleanExpression eq(StringPath path, String keyword) {
    return (keyword == null || keyword.isBlank()) ? null : path.eq(keyword.trim());
  }

  // eq(null-safe)
  public static <T> BooleanExpression eqIfNotNull(SimpleExpression<T> path, T value) {
    return value == null ? null : path.eq(value);
  }

  // boolean eq(null-safe)
  public static BooleanExpression eqIfNotNull(BooleanPath path, Boolean value) {
    return value == null ? null : path.eq(value);
  }

  // in(null-safe)
  public static <T> BooleanExpression inIfNotNull(SimpleExpression<T> path, Collection<T> values) {
    return (values == null || values.isEmpty()) ? null : path.in(values);
  }

  public static BooleanExpression IsNotNull(Expression<?> path) {
    return path != null ? ((SimpleExpression<?>) path).isNotNull() : null;
  }

  public static BooleanExpression IsNull(Expression<?> path) {
    return path != null ? ((SimpleExpression<?>) path).isNull() : null;
  }

  public static BooleanExpression betweenIfNotNull(DateTimePath<LocalDateTime> path,
                                                   LocalDateTime from,
                                                   LocalDateTime to) {
    if (from != null && to != null) {
      return path.between(from, to);
    }
    if (from != null) {
      return path.goe(from);
    }
    if (to != null) {
      return path.loe(to);
    }
    return null;
  }

  public static BooleanExpression betweenIfNotNull(NumberPath<Integer> path,
                                                   Integer from,
                                                   Integer to) {
    if (from != null && to != null) {
      return path.between(from, to);
    }
    if (from != null) {
      return path.goe(from);
    }
    if (to != null) {
      return path.loe(to);
    }
    return null;
  }


}


