package com.book.laboratory.common.redis;

import com.book.laboratory.common.exception.CommonErrorCode;
import com.book.laboratory.common.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "RedisServiceImpl")
@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public <T> void set(String key, T value, Duration ttl) {
    try {
      String json = objectMapper.writeValueAsString(value);
      redisTemplate.opsForValue().set(key, json, ttl);
    } catch (JsonProcessingException e) {
      throw new CustomException(CommonErrorCode.FAILED_SET_REDIS);
    }
  }

  @Override
  public <T> T get(String key, Class<T> clazz) {
    try {
      String json = redisTemplate.opsForValue().get(key);
      return json == null ? null : objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new CustomException(CommonErrorCode.FAILED_GET_REDIS);
    }
  }

  @Override
  public void delete(String key) {
    redisTemplate.delete(key);
  }

  @Override
  public boolean exists(String key) {
    return redisTemplate.hasKey(key);
  }
}
