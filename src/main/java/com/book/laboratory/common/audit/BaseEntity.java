package com.book.laboratory.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
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
  @Column(name = "created_at", updatable = false)
  protected LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  protected Long createdBy;

  @LastModifiedDate
  @Column(name = "modified_at")
  protected LocalDateTime modifiedAt;

  @LastModifiedBy
  @Column(name = "modified_by")
  protected Long modifiedBy;

  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

  @Column(name = "deleted_by")
  protected Long deletedBy;

  public void markDeleted(Long userId) {
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = userId;
  }

  public boolean isDeleted() {
    return this.deletedAt != null;
  }
}
