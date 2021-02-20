package com.devflix.controller;

import com.devflix.domain.JoinUsDomain;
import com.devflix.entity.Member;
import com.devflix.service.MemberService;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "/login/index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginAction(HttpServletRequest request, @RequestParam(name = "email", required = false)String email,
                              @RequestParam(name = "password", required = false)String password, RedirectAttributes attrs) {
        if (StringUtils.isBlank(email)) {
            attrs.addFlashAttribute("errorMessage", "이메일 기입해 주세요.");

            return "redirect:/login?error";
        } else if (StringUtils.isBlank(password)) {
            attrs.addFlashAttribute("errorMessage", "패스워드 기입해 주세요.");

            return "redirect:/login?error";
        }

        AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (exception != null) {
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

        memberService.createMember(domain);

        return "redirect:/login";
    }

    @RequestMapping(path = "/login/join-us/email-authentication", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> emailValidate(@RequestParam(value = "email", required = false)final String email) {
        if (StringUtils.isBlank(email)) {
            return ImmutableMap.of("result", false, "msg", "이메일을 기입해 주세요.");
        }

        Member findUser = memberService.findUserByEmail(email);

        if (findUser != null) {
            return ImmutableMap.of("result", false, "msg", "이미 존재하는 아이디 입니다.");
        }

        memberService.emailConfirm(email);

        return ImmutableMap.of("result", true, "msg", "인증 메일 성공적으로 요청 하였습니다.");
    }
}
