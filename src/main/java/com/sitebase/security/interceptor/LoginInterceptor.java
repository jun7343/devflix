package com.sitebase.security.interceptor;

import com.sitebase.constant.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            Collection<? extends GrantedAuthority> authorities =
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            AtomicBoolean isAuthentication = new AtomicBoolean(true);

            authorities.forEach(grantedAuthority -> {
                if (grantedAuthority.getAuthority().equals(RoleType.ANONYMOUS)) {
                    isAuthentication.set(false);
                }
            });

            modelAndView.addObject("isAuthentication", isAuthentication.get());
        }
    }
}
