package com.book.laboratory.user.infrastructure.user;

import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
}
