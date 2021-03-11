package kr.devflix.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminDashBoardController {

    @RequestMapping(path = "/dfa", method = RequestMethod.GET)
    public String index() {
        return "admin/dashboard";
    }
}
