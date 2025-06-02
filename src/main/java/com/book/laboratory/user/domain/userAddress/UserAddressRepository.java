package com.book.laboratory.user.domain.userAddress;

import java.util.Optional;

public interface UserAddressRepository {

  UserAddress save(UserAddress userAddress);

  Optional<UserAddress> findUserAddressById(Long id);


}
