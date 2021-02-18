package com.devflix.controller;

import com.devflix.domain.JoinUsDomain;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.devflix.dto.MemberDto;
import com.devflix.entity.Member;
import com.devflix.service.LoginService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "/login/index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginAction(HttpServletRequest request, RedirectAttributes attrs) {
        AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (exception != null) {
            System.out.println("error message = " + exception.getMessage());
            attrs.addFlashAttribute("error", true);
            attrs.addFlashAttribute("errorMessage", exception.getMessage());

            return "redirect:/login?error";
        }

        return "home";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.GET)
    public String JoinUsForm() {
        return "/login/join-us";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.POST)
    public String JoinUsAction(JoinUsDomain domain, RedirectAttributes attrs) {
        String message = "";
        boolean success = false;

        if (StringUtils.isBlank(domain.getEmail())) {
            message = "이메일 기입해 주세요.";
        } else if (StringUtils.isBlank(domain.getUsername())) {
            message = "유저이름 기입해 주세요.";
        } else if (StringUtils.isBlank(domain.getPassword())) {
            message = "패스워드 기입해 주세요.";
        } else {
            success = true;
        }

        if (! success) {
            attrs.addFlashAttribute("errorMessage", message);

            return "redirect:/login/join-us";
        }

        loginService.createMember(domain);

        return "redirect:/login";
    }
}
