package kr.devflix.admin.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.job.CrawlingScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Secured(RoleType.MANAGER)
@RequiredArgsConstructor
public class AdminDashBoardController {

    private final CrawlingScheduler crawlingScheduler;

    @RequestMapping(path = "/dfa", method = RequestMethod.GET)
    public String index() {
        return "admin/dashboard";
    }

    @RequestMapping(path = "/dfa/ttt", method = RequestMethod.GET)
    @ResponseBody
    public String ttt() {
        crawlingScheduler.crawlingAll();

        return "asdasd";
    }
}
