package com.book.laboratory.common.filter;


import com.book.laboratory.common.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j(topic = "JwtTokenFilter")
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  /**
   * Filters incoming HTTP requests to validate JWT tokens and set authentication in the security context.
   *
   * If a valid JWT token is present in the "Authorization" header, the user's authentication is established in the Spring Security context. If the token is missing, invalid, or improperly formatted, the response is set to 401 Unauthorized with a JSON error message.
   *
   * @param request the incoming servlet request
   * @param response the outgoing servlet response
   * @param filterChain the filter chain to proceed with the request
   * @throws IOException if an input or output error occurs
   * @throws ServletException if a servlet error occurs
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    log.info("doFilter 진입!!!!!!!!");
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String token = httpRequest.getHeader("Authorization");
    log.info("doFilter token: {}", token);

    try {

      if (token != null) {
        if (!token.startsWith("Bearer ")) {
          throw new AuthenticationException("Bearer 형식이 아닙니다.");
        }

        // 토큰 검증 및 클레임(페이로드) 추출
        String jwtToken = token.substring(7);
        log.info("jwtToken: {}", jwtToken);
        Claims claims = Jwts.parser()
            .verifyWith(jwtTokenProvider.getSecretKey())
            .build()
            .parseSignedClaims(jwtToken)
            .getPayload();
        log.info("claims : {}", claims.toString());
        log.info("jwt : {}", jwtToken);

        // Authentication 객체 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(claims.get("role").toString()));
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, jwtToken,
            userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      filterChain.doFilter(request, response);

    } catch (Exception e) {
      log.info("doFilter error: {}", e.getMessage());
      httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
      httpResponse.setContentType("application/json");
      httpResponse.getWriter().write("invalid token");
    }
  }
}
