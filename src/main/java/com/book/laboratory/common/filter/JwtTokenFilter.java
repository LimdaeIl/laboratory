package com.book.laboratory.common.filter;


import com.book.laboratory.common.jwt.JwtTokenProvider;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.user.domain.user.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j(topic = "JwtTokenFilter")
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String bearerToken = httpRequest.getHeader("Authorization");

    try {
      if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        Claims claims = jwtTokenProvider.extractClaims(bearerToken);

        Long userId = claims.get("userId", Long.class);
        String email = claims.getSubject(); // or claims.get("email", String.class)
        String role = claims.get("role", String.class);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        CustomUserDetails userDetails = new CustomUserDetails(userId, email, UserRole.valueOf(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, bearerToken, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      filterChain.doFilter(request, response);

    } catch (Exception e) {
      log.warn("JWT 인증 실패: {}", e.getMessage());
      httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
      httpResponse.setContentType("application/json");
      httpResponse.getWriter().write("invalid token");
    }
  }
}
