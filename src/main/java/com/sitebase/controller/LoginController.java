package com.sitebase.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sitebase.dto.MemberDto;
import com.sitebase.entity.Member;
import com.sitebase.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "/login/index";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.GET)
    public String JoinUsForm() {
        return "/login/join-us";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.POST)
    public String JoinUsAction(MemberDto dto, RedirectAttributes attrs) {
        Member user = loginService.createMember(dto);

        if (user == null) {
            attrs.addFlashAttribute("msg", "이미 생성된 아이디 입니다.");

            return "redirect:/login/join-us";
        }

        attrs.addFlashAttribute("msg", "회원가입 완료 하셨습니다.");

        return "redirect:/login";
    }

    // 아이디 중복 확인 API 아이디 중복 확인시 true 반환
    @RequestMapping(path = "/login/username-confirm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONPObject confirmUsername(@RequestParam(name = "username")String username) {
        Member member = loginService.findMember(username);

        return new JSONPObject("result", member == null);
    }
}
