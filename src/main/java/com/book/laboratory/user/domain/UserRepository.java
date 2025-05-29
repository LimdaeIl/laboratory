package com.book.laboratory.user.domain;

public interface UserRepository {
  /****
 * Checks if a user with the specified email exists.
 *
 * @param email the email address to check for existence
 * @return true if a user with the given email exists, false otherwise
 */
boolean existsUserByEmail(String email);

  /****
 * Persists the given user and returns the saved instance.
 *
 * @param user the user entity to be saved
 * @return the saved user entity
 */
User save(User user);
}
