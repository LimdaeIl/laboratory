package com.book.laboratory.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  ROLE_ADMIN("관리자"),
  ROLE_STORE("점주"),
  ROLE_USER("회원");

  private final String name;


  public boolean isUser() {
    return this == ROLE_USER;
  }

  public boolean isStore() {
    return this == ROLE_STORE;
  }

  public boolean isAdmin() {
    return this == ROLE_ADMIN;
  }
}
