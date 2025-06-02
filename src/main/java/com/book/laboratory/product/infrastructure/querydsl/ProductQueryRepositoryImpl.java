package com.book.laboratory.product.infrastructure.querydsl;

import com.book.laboratory.common.querydsl.QuerydslSortUtil;
import com.book.laboratory.common.querydsl.QuerydslWhereUtil;
import com.book.laboratory.product.application.dto.condition.ProductSearchCondition;
import com.book.laboratory.product.application.dto.response.GetProductsResponseDto;
import com.book.laboratory.product.domain.entity.QProduct;
import com.book.laboratory.product.domain.repository.ProductQueryRepository;
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
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<GetProductsResponseDto> findProductsByCondition(ProductSearchCondition condition, Pageable pageable) {
    QProduct product = QProduct.product;

    BooleanBuilder where = new BooleanBuilder()
        .and(QuerydslWhereUtil.like(product.name, condition.name()))
        .and(QuerydslWhereUtil.betweenIfNotNull(product.price, condition.priceFrom(), condition.priceTo()))
        .and(QuerydslWhereUtil.eqIfNotNull(product.category, condition.category()));

    List<OrderSpecifier<?>> orderSpecifiers = QuerydslSortUtil.toOrderSpecifiers(
        pageable.getSort(),
        product.getClass(),
        "product",
        Set.of(
            "name",
            "price",
            "category",
            "createdAt"
        )
    );

    List<GetProductsResponseDto> products = jpaQueryFactory
        .select(Projections.constructor(
            GetProductsResponseDto.class,
            product.name,
            product.price,
            product.category,
            product.thumbnail
        )).from(product)
        .where(where)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = jpaQueryFactory
        .select(product.count())
        .from(product)
        .where(where)
        .fetchOne();

    return new PageImpl<>(products, pageable, total != null ? total : 0);
  }
}
