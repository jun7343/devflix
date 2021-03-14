package kr.devflix.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.RequestDispatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //정적 파일 Authentication 패스
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureHandler((request, response, exception) -> {
                    if (exception instanceof BadCredentialsException) {
                        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new BadCredentialsException("아이디 또는 비밀번호가 다릅니다."));
                    } else if (exception instanceof InternalAuthenticationServiceException) {
                        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new InternalAuthenticationServiceException("아이디가 존재하지 않습니다."));
                    } else if (exception instanceof LockedException) {
                        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new LockedException("계정이 잠금 상태 입니다."));
                    } else if (exception instanceof DisabledException) {
                        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new DisabledException("회원 탈퇴한 계정 입니다."));
                    } else if (exception instanceof AccountExpiredException) {
                        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new AccountExpiredException("만료된 계정 입니다."));
                    } else if (exception instanceof CredentialsExpiredException) {
                        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new CredentialsExpiredException("만료된 비밀번호 입니다."));
                    }

                    RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
                    dispatcher.forward(request, response);
                })
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
