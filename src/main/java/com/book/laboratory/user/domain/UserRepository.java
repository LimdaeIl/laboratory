package com.book.laboratory.user.domain;

public interface UserRepository {
  boolean existsUserByEmail(String email);

  User save(User user);
}
