package com.book.laboratory.user.domain;

import com.book.laboratory.common.audit.BaseEntity;
import com.book.laboratory.common.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_users")
@Entity
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private Long id;

  @Column(name = "name", nullable = false, length = 10)
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
  @Column(name = "social_type")
  private SocialType socialType;

  @Column(name = "social_id", length = 200)
  private String socialId;


  public void updatePassword(String newPassword) {
    if (newPassword == null || newPassword.isBlank()) {
      throw new CustomException(UserErrorCode.INVALID_PASSWORD);
    }
    this.password = newPassword;
  }

  public void updateProfileImageUrl(String newProfileImageUrl) {
    if (newProfileImageUrl == null || newProfileImageUrl.isBlank()) {
      newProfileImageUrl = null;
    }
    this.profileImageUrl = newProfileImageUrl;
  }

  public void updateName(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new CustomException(UserErrorCode.INVALID_USER_NAME);
    }
    this.name = newName;
  }

  public void updateUserRole(UserRole newUserRole) {
    if (newUserRole == null) {
      throw new CustomException(UserErrorCode.INVALID_USER_ROLE);
    }
    this.userRole = newUserRole;
  }

  public void updateUserEmail(String newEmail) {
    if (newEmail == null || newEmail.isBlank()) {
      throw new CustomException(UserErrorCode.INVALID_PATCH_EMAIL);
    }

    this.email = newEmail;
  }


}
