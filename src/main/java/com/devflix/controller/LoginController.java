package com.devflix.controller;

import com.devflix.domain.JoinUsDomain;
import com.devflix.entity.Member;
import com.devflix.entity.MemberConfirm;
import com.devflix.service.MemberService;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

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
    public String joinUsForm() {
        return "/login/join-us";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.POST)
    public String joinUsAction(JoinUsDomain domain, RedirectAttributes attrs) {
        String message = "";
        boolean success = false;

        if (StringUtils.isBlank(domain.getEmail())) {
            message = "이메일 기입해 주세요.";
        } else if (StringUtils.isBlank(domain.getUsername())) {
            message = "유저 이름 기입해 주세요.";
        } else if (StringUtils.isBlank(domain.getPassword())) {
            message = "패스워드 기입해 주세요.";
        } else if (StringUtils.isBlank(domain.getCode())) {
            message = "인증코드가 기입해 주세요.";
        }

        MemberConfirm confirm = memberService.findMemberConfirmByEmail(domain.getEmail());

        if (confirm == null) {
            message = "인증 확인해 주세요.";
        } else if (! StringUtils.equals(confirm.getUuid(), domain.getCode())) {
            message = "인증코드가 다릅니다. 다시 확인해 주세요.";
        } else {
            Member newMember = memberService.createMemberAndDeleteMemberConfirm(domain);

            if (newMember == null) {
                message = "아이디 생성중 에러가 발생하였습니다. 관리자에게 문의해 주세요.";
            } else {
                success = true;
            }
        }

        if (! success) {
            attrs.addFlashAttribute("errorMessage", message);

            return "redirect:/login/join-us";
        }

        return "redirect:/login";
    }

    @RequestMapping(path = "/login/join-us/email-authentication", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> emailAuthentication(@RequestParam(value = "email", required = false)final String email) {
        final String RESULT = "result";
        final String MESSAGE = "msg";

        if (StringUtils.isBlank(email)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일을 기입해 주세요.");
        }

        Member findUser = memberService.findUserByEmail(email);

        if (findUser != null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이미 가입한 회원 입니다.");
        }

        MemberConfirm findConfirm = memberService.findMemberConfirmByEmail(email);

        if (findConfirm != null && findConfirm.getConfirmCount() >= 5) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "요청 메일 횟수를 넘어섰습니다.");
        }

        MemberConfirm confirm = memberService.createOrUpdateMemberConfirmByEmail(email);

        if (confirm == null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증 과정중 에러가 발생하였습니다. 관리자에게 문의해 주세요.");
        }

        return ImmutableMap.of(RESULT, true, MESSAGE, "인증 메일 성공적으로 요청 하였습니다.");
    }

    @RequestMapping(path = "/login/join-us/code-authentication", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> codeAuthentication(@RequestParam(name = "email", required = false)final String email,
                                                           @RequestParam(name = "code", required = false)final String code) {
        final String RESULT = "result";
        final String MESSAGE = "msg";

        if (StringUtils.isBlank(email)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 기입해 주세요.");
        } else if (StringUtils.isBlank(code)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증코드 기입해 주세요.");
        }

        MemberConfirm confirm = memberService.findMemberConfirmByEmail(email);

        if (confirm == null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 인증 다시 확인해 주세요.");
        } else if (! StringUtils.equals(confirm.getUuid(), code)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증코드가 맞지 않습니다. 다시 확인해 주세요.");
        }

        return ImmutableMap.of(RESULT, true, MESSAGE, "인증 확인 되었습니다.");
    }

    @RequestMapping(path = "/login/join-us/all-confirm", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> allConfirm(@RequestParam(name = "email", required = false)final String email,
                                                   @RequestParam(name = "code", required = false)final String code) {
        final String RESULT = "result";
        final String MESSAGE = "msg";

        if (StringUtils.isBlank(email)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 기입해 주세요.");
        } else if (StringUtils.isBlank(code)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증코드 기입해 주세요.");
        }

        Member findUser = memberService.findUserByEmail(email);

        if (findUser != null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이미 가입한 회원 입니다.");
        }

        MemberConfirm findConfirm = memberService.findMemberConfirmByEmail(email);

        if (findConfirm == null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 인증해 주세요.");
        } else if (! StringUtils.equals(findConfirm.getUuid(), code) && ! StringUtils.equals(findConfirm.getEmail(), email)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증 코드 확인해 주세요.");
        }

        return ImmutableMap.of(RESULT, true, MESSAGE, "가입 완료 되었습니다.");
    }
}
