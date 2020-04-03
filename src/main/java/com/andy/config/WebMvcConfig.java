package com.andy.config;

import com.andy.controller.SecurityHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final SecurityHandler securityHandler;

  public WebMvcConfig(SecurityHandler securityHandler) {
    this.securityHandler = securityHandler;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(securityHandler);
  }

}
