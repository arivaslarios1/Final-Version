// /backend/src/main/java/com/example/InvProject/CorsConfig.java
package com.example.InvProject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry reg) {
    reg.addMapping("/api/**")
      // allow localhost/127.0.0.1 on any port (Live Server varies)
      .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
      .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
      .allowedHeaders("*");
  }
}
