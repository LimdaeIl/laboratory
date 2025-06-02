package com.book.laboratory.user.infrastructure.user;

import com.book.laboratory.common.querydsl.QuerydslSortUtil;
import com.book.laboratory.common.querydsl.QuerydslWhereUtil;
import com.book.laboratory.user.application.user.condition.UserSearchCondition;
import com.book.laboratory.user.application.user.dto.response.GetUsersResponseDto;
import com.book.laboratory.user.domain.user.QUser;
import com.book.laboratory.user.domain.user.UserQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserQueryRepositoryImpl implements UserQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<GetUsersResponseDto> findUsersByCondition(UserSearchCondition condition, Pageable pageable) {
    QUser user = QUser.user;

    BooleanBuilder where = new BooleanBuilder()
        .and(QuerydslWhereUtil.eqIfNotNull(user.id, condition.id()))
        .and(QuerydslWhereUtil.like(user.name, condition.name()))
        .and(QuerydslWhereUtil.like(user.email, condition.email()))
        .and(QuerydslWhereUtil.eqIfNotNull(user.userRole, condition.userRole()))
        .and(QuerydslWhereUtil.eqIfNotNull(user.socialType, condition.socialType()))
        .and(QuerydslWhereUtil.eqIfNotNull(user.socialId, condition.socialId()))
        .and(QuerydslWhereUtil.betweenIfNotNull(user.createdAt, condition.createdFrom(), condition.createdTo()))
        .and(QuerydslWhereUtil.betweenIfNotNull(user.modifiedAt, condition.updatedFrom(), condition.updatedTo()))
        .and(QuerydslWhereUtil.betweenIfNotNull(user.deletedAt, condition.deletedFrom(), condition.deletedTo()))
        .and(QuerydslWhereUtil.eqIfNotNull(user.createdBy, condition.createdBy()))
        .and(QuerydslWhereUtil.eqIfNotNull(user.createdBy, condition.createdBy()))
        .and(QuerydslWhereUtil.eqIfNotNull(user.createdBy, condition.createdBy()));

    List<OrderSpecifier<?>> orderSpecifiers = QuerydslSortUtil.toOrderSpecifiers(
        pageable.getSort(),
        user.getClass(),
        "user",
        Set.of(
            "id",
            "name",
            "email",
            "profileImageUrl",
            "userRole",
            "socialType",
            "socialId",
            "createdAt",
            "createdBy",
            "updatedAt",
            "updatedBy",
            "deletedAt",
            "deletedBy"
        )
    );

    List<GetUsersResponseDto> users = jpaQueryFactory.select(user)
        .select(Projections.constructor(
            GetUsersResponseDto.class,
            user.id,
            user.name,
            user.email,
            user.profileImageUrl,
            user.userRole,
            user.socialType,
            user.socialId,
            user.createdAt,
            user.createdBy,
            user.modifiedAt,
            user.modifiedBy,
            user.deletedAt,
            user.deletedBy
        )).from(user)
        .where(where)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = jpaQueryFactory
        .select(user.count())
        .from(user)
        .where(where)
        .fetchOne();

    return new PageImpl<>(users, pageable, total != null ? total : 0);
  }
}
