package com.book.laboratory.user.domain.userAddress;

import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository {

  UserAddress save(UserAddress userAddress);

  Optional<UserAddress> findUserAddressById(UUID id);


}
