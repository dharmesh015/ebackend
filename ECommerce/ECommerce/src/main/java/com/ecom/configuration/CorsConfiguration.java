//package com.ecom.configuration;
//
//import org.aspectj.weaver.loadtime.Options;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfiguration {
//
//    private static final String GET = "GET";
//    private static final String POST = "POST";
//    private static final String PUT = "PUT";
//    private static final String DELETE = "DELETE";
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedMethods(GET, POST, PUT, DELETE)
//                        .allowedHeaders("*")
//                        .allowedOrigins("http://localhost:4200")
//                        .allowCredentials(true);
//            }
//        };
//    }
//}
package com.ecom.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // Allow requests from this origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow credentials
            }
        };
    }
}
