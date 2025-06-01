package com.book.laboratory.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI getOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Laboratory API 문서")
            .description("API 명세서입니다.")
            .version("1.0.0"));
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("Laboratory API")
        .pathsToMatch("/api/**")
        .build();
  }

}
