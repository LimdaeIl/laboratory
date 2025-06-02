package com.book.laboratory.user.infrastructure.userAddress;

import com.book.laboratory.user.domain.userAddress.UserAddress;
import com.book.laboratory.user.domain.userAddress.UserAddressRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressJpaRepository extends JpaRepository<UserAddress, UUID>, UserAddressRepository {

}
