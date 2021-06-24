package kr.devflix.controller;

import kr.devflix.posts.DevPost;
import kr.devflix.posts.DevPostService;
import kr.devflix.posts.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DevPostService devPostService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(@RequestParam(name = "page", required = false, defaultValue = "0")int page, Model model) {
        final DevPost banner = devPostService.findRandomOneByStatus(Status.POST);

        model.addAttribute("banner", banner);

        return "home";
    }
}
