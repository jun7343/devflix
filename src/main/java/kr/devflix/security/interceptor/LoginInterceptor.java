package kr.devflix.security.interceptor;

import kr.devflix.constant.RoleType;
import kr.devflix.entity.Member;
import kr.devflix.entity.PostCommentAlert;
import kr.devflix.service.PostCommentAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final PostCommentAlertService postCommentAlertService;

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

            if (isAuthentication.get()) {
                modelAndView.addObject("user", authentication.getPrincipal());

                modelAndView.addObject("commentAlert", postCommentAlertService.findAllByConfirmAndUser((Member) authentication.getPrincipal()));
            }

            DefaultCsrfToken token = (DefaultCsrfToken) request.getAttribute("_csrf");

            modelAndView.addObject("meta-url", request.getRequestURL());
            modelAndView.addObject("csrf_parameter", token.getParameterName());
            modelAndView.addObject("csrf_header", token.getHeaderName());
            modelAndView.addObject("csrf_token", token.getToken());
        }
    }
}
