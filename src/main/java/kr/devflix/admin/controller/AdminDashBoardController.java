package kr.devflix.admin.controller;

import kr.devflix.constant.RoleType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Secured(RoleType.MANAGER)
public class AdminDashBoardController {

    @RequestMapping(path = "/dfa", method = RequestMethod.GET)
    public String index() {
        return "admin/dashboard";
    }
}
