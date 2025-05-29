package com.book.laboratory.user.infrastructure;

import com.book.laboratory.user.domain.User;
import com.book.laboratory.user.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
}
