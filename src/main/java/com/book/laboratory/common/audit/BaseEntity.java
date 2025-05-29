package com.book.laboratory.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @CreatedDate
  @Column(updatable = false)
  protected LocalDateTime createdAt;

  @CreatedBy
  @Column(updatable = false)
  protected Long createdBy;

  @LastModifiedDate
  protected LocalDateTime modifiedAt;

  @LastModifiedBy
  protected Long modifiedBy;

  protected LocalDateTime deletedAt;

  protected Long deletedBy;

  public void markDeleted(Long userId) {
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = userId;
  }

  public boolean isDeleted() {
    return this.deletedAt != null;
  }
}
