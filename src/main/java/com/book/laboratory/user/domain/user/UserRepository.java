package com.book.laboratory.user.domain.user;

import java.util.Optional;

public interface UserRepository {
  boolean existsUserByEmail(String email);

  User save(User user);

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserById(Long id);
}
