package com.devflix.config;

import com.devflix.security.interceptor.LoginInterceptor;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Collections;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.devflix")
@EnableJpaRepositories(basePackages = "com.devflix.repository")
@EnableScheduling
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if (environment.acceptsProfiles(Profiles.of("local"))) {
            registry.addResourceHandler("/assets/**").addResourceLocations("file:src/main/resources/assets/");
            registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
        } else if (environment.acceptsProfiles(Profiles.of("dev"))) {
            registry.addResourceHandler("/assets/**").addResourceLocations("file:/srv/devflix/assets/");
        } else {
            registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/assets/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost", "http://15.164.231.154");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new XssEscapeServletFilter());
        registrationBean.setUrlPatterns(Collections.singletonList("/post/write"));
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
