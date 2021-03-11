package kr.devflix.security.interceptor;

import kr.devflix.constant.RoleType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) return;

            AtomicBoolean isAuthentication = new AtomicBoolean(true);

            authentication.getAuthorities().forEach(grantedAuthority -> {
                if (grantedAuthority.getAuthority() == null ||
                        grantedAuthority.getAuthority().equals(RoleType.ANONYMOUS)) {
                    isAuthentication.set(false);
                }
            });

            modelAndView.addObject("isAuthentication", isAuthentication.get());

            if (isAuthentication.get()) {
                modelAndView.addObject("user", authentication.getDetails());
            }
        }
    }
}
