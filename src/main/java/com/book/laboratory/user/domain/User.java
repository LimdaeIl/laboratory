package com.book.laboratory.user.domain;

import com.book.laboratory.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_users")
@Entity
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "password", length = 512)
  private String password;

  @Column(name = "profile_image_url", length = 512)
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  @Column(name = "user_role", nullable = false)
  private UserRole userRole = UserRole.ROLE_USER;

  @Enumerated(EnumType.STRING)
  @Column(name = "soical_type")
  private SocialType socialType;

  @Column(name = "social_id", length = 200)
  private String socialId;

}
