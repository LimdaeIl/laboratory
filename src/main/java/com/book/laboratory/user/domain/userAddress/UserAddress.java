package com.book.laboratory.user.domain.userAddress;

import com.book.laboratory.common.audit.BaseEntity;
import com.book.laboratory.user.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user_address")
@Entity
public class UserAddress extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "zone_code", nullable = false)
  private Integer zoneCode;

  @Column(name = "road_address", nullable = false, length = 100)
  private String roadAddress;

  @Column(name = "add_address",nullable = false, length = 100)
  private String addAddress;
}
