package com.sitebase.configuration;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.sitebase.security.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.sitebase")
public class WebConfig implements WebMvcConfigurer {

    final String CLASSPATH_RESOURCE_LOCATIONS = "classpath:";

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS + "/assets/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/assets/**");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(viewResolver());
    }

    @Bean
    public HandlebarsViewResolver viewResolver() {
        HandlebarsViewResolver viewResolver = new HandlebarsViewResolver();

        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".hbs");
        viewResolver.setCache(false);
        viewResolver.setCharset(StandardCharsets.UTF_8);

        return viewResolver;
    }
}
