package com.sitebase.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.sitebase")
public class WebConfig implements WebMvcConfigurer {

    private Environment env;

    WebConfig(Environment env) {
        this.env = env;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webapp");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(viewResolver());
    }

    @Bean
    public MustacheViewResolver viewResolver() {
        MustacheViewResolver resolver = new MustacheViewResolver();

        resolver.setContentType("html/text; charset=utf-8");
        resolver.setSuffix(".mustache");
        resolver.setPrefix(env.getProperty("template.root", "file:./template"));

        return resolver;
    }
}
