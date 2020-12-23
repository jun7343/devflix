package com.sitebase.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sitebase.command.MemberCommand;
import com.sitebase.service.LoginService;
import com.sitebase.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "/login/index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginAction() {
        return "redirect:home";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.GET)
    public String JoinUsForm() {
        return "/login/join-us";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.POST)
    public String JoinUsAction(MemberCommand command, RedirectAttributes attrs) {
        System.out.println(command.toString());
        Result result = loginService.createMember(command);

        if (result.isERROR()) {
            attrs.addFlashAttribute("msg", result.getMessage());

            return "redirect:/login/join-us";
        }

        return "redirect:/login";
    }

    // 아이디 중복 확인 API 아이디 중복 확인시 true 반환
    @RequestMapping(path = "/login/id-check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Boolean> confirmUserId(@RequestParam(name = "id")String id) {
        Boolean result = loginService.getUser(id);

        Map<String, Boolean> map = new HashMap<>();

        map.put("result", result);

        return map;
    }
}
