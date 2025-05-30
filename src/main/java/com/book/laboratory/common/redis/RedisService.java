package com.book.laboratory.common.redis;

import java.time.Duration;

public interface RedisService {
  <T> void set(String key, T value, Duration ttl);
  <T> T get(String key, Class<T> clazz);
  void delete(String key);
  boolean exists(String key);
}
