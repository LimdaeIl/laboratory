package com.book.laboratory.product.domain.entity;

import com.book.laboratory.common.audit.BaseEntity;
import com.book.laboratory.common.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_products")
@Entity
public class Product extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "thumbnail")
  private String thumbnail;

  @Lob
  @Column(name = "description", columnDefinition = "TEXT", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ProductStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private ProductCategory category;

  public void changeNameTo(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new CustomException(ProductErrorCode.INVALID_PRODUCT_NAME);
    }

    this.name = newName;
  }

  public void changePriceTo(Integer newPrice) {
    if (newPrice == null || newPrice < 0) {
      throw new CustomException(ProductErrorCode.INVALID_PRODUCT_PRICE);
    }
    this.price = newPrice;
  }


  public void changeQuantityTo(Integer amount) {
    if (amount == null || amount < 0) {
      throw new CustomException(ProductErrorCode.INVALID_PRODUCT_QUANTITY);
    }

    this.quantity = amount;
  }

  public void updateThumbnail(String newThumbnail) {
    if (newThumbnail == null || newThumbnail.isBlank()) {
      this.thumbnail = null;
      return;
    }
    this.thumbnail = newThumbnail;
  }

  public void changeDescriptionTo(String newDescription) {
    if (newDescription == null || newDescription.isBlank()) {
      throw new CustomException(ProductErrorCode.INVALID_PRODUCT_DESCRIPTION);
    }
    this.description = newDescription;
  }

  public void changeStatusTo(ProductStatus newStatus) {
    if (newStatus == null) {
      throw new CustomException(ProductErrorCode.INVALID_PRODUCT_STATUS);
    }
    this.status = newStatus;
  }

  public void changeCategoryTo(ProductCategory newCategory) {
    if (newCategory == null) {
      throw new CustomException(ProductErrorCode.INVALID_PRODUCT_CATEGORY);
    }
    this.category = newCategory;
  }
}
