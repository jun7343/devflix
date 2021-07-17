package kr.devflix.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.entity.Member;
import kr.devflix.entity.PostCommentAlert;
import kr.devflix.service.PostCommentAlertService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostCommentAlertController {

    private final PostCommentAlertService postCommentAlertService;
    private final int DEFAULT_SIZE_VALUE = 20;

    public PostCommentAlertController(PostCommentAlertService postCommentAlertService) {
        this.postCommentAlertService = postCommentAlertService;
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/comment-alert", method = RequestMethod.GET)
    public String index(@AuthenticationPrincipal Member user, @RequestParam(name = "page", required = false, defaultValue = "0")final int page,
                        Model model) {
        Page<PostCommentAlert> findList = postCommentAlertService.findAllByUserOrderByCreateAtDesc(user, page, DEFAULT_SIZE_VALUE);
        List<Integer> pageNumList = new ArrayList<>();

        model.addAttribute("list", findList);

        if (findList.getNumber() / 5 != 0 && ((findList.getNumber() / 5) * 5 - 1) > 0) {
            model.addAttribute("previousPageNum", (findList.getNumber() / 5) * 5 - 1);
        }

        if ((findList.getNumber() / 5) * 5 + 6 <= findList.getTotalPages()) {
            model.addAttribute("nextPageNum", (findList.getNumber() / 5 + 1) * 5);
        }

        int start = (findList.getNumber() / 5) * 5 + 1;
        int end = Math.min((findList.getNumber() / 5 + 1) * 5, findList.getTotalPages());

        for (int i = start; i <= end; i++) {
            pageNumList.add(i);
        }

        model.addAttribute("pageNumList", pageNumList);
        model.addAttribute("currentPageNum", findList.getNumber() + 1);
        model.addAttribute("pagination", findList.getTotalPages() > 1);

        return "/comment-alert/list";
    }
}
