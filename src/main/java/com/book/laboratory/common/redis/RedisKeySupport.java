package com.book.laboratory.common.redis;

public final class RedisKeySupport {

  private RedisKeySupport() {
  } // 유틸리티 클래스: 생성자 막기

  // ==== 인증: R: Refresh, B: Blacklist, E: Email ====
  public static String refreshToken(String jti) {
    return "RT:" + jti;
  }

  public static String BlacklistToken(String jti) {
    return "BL:" + jti;
  }

  public static String emailVerification(String email) {
    return "EC:" + email;
  }

  // ==== 캐시: C ====
  public static String productCache(Long productId) {
    return "CP:" + productId;
  }

  public static String userProfile(Long userId) {
    return "CU" + userId;
  }

  // ==== 락: L ====
  public static String couponLock(Long couponId) {
    return "LC:" + couponId;
  }

  // ==== 기타 ====
  public static String rateLimit(String ip) {
    return "ratelimit:ip:" + ip;
  }
}
