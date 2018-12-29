package com.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Feinik
 * @Discription
 * @Data 2018/12/25
 * @Version 1.0.0
 */
@Configuration
@ComponentScan(basePackages = "com")
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 启用默认的静态资源访问
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


}
