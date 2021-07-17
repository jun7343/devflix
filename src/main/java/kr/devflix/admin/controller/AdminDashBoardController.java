package kr.devflix.admin.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.job.CrawlingScheduler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Secured(RoleType.MANAGER)
public class AdminDashBoardController {

    private final CrawlingScheduler crawlingScheduler;

    public AdminDashBoardController(CrawlingScheduler crawlingScheduler) {
        this.crawlingScheduler = crawlingScheduler;
    }

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
