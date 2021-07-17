package kr.devflix.admin.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.entity.PostComment;
import kr.devflix.service.PostCommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Secured(RoleType.MANAGER)
public class AdminPostCommentController {

    private final PostCommentService postCommentService;
    private final int DEFAULT_PER_PAGE_SIZE = 20;

    public AdminPostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @RequestMapping(path = "/dfa/post/post-comment", method = RequestMethod.GET)
    public String postCommentList(@RequestParam(name = "page", required = false, defaultValue = "0")final int page,
                                  @RequestParam(name = "comment", required = false)final String comment,
                                  @RequestParam(name = "writer", required = false)final String writer,
                                  @RequestParam(name = "status", required = false)Status status, Model model) {
        Page<PostComment> findList = null;

        if (StringUtils.isBlank(comment) && StringUtils.isBlank(writer) && status == null) {
            findList = postCommentService.findAll(page, DEFAULT_PER_PAGE_SIZE);
        } else {
            findList = postCommentService.findAllBySearch(comment, writer, status, page, DEFAULT_PER_PAGE_SIZE);
        }

        model.addAttribute("search-comment", comment);
        model.addAttribute("search-writer", writer);
        model.addAttribute("search-status", status);

        List<Integer> pageNumList = new ArrayList<>();

        model.addAttribute("list", findList);
        model.addAttribute("page", page);

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

        return "/admin/post/post-comment";
    }

    @RequestMapping(path = "/dfa/post/post-comment/post", method = RequestMethod.POST)
    public String postCommentPost(@RequestParam(name = "ids", required = false)List<Long> idList,
                                    @RequestParam(name = "page", required = false, defaultValue = "0")final int page) {
        if (idList != null) {
            postCommentService.updateStatusByIdList(Status.POST, idList);
        }

        return "redirect:/dfa/post/post-comment?page=" + page;
    }

    @RequestMapping(path = "/dfa/post/post-comment/hidden", method = RequestMethod.POST)
    public String postCommentHidden(@RequestParam(name = "ids", required = false)List<Long> idList,
                                    @RequestParam(name = "page", required = false, defaultValue = "0")final int page) {
        if (idList != null) {
            postCommentService.updateStatusByIdList(Status.HIDDEN, idList);
        }

        return "redirect:/dfa/post/post-comment?page=" + page;
    }

    @RequestMapping(path = "/dfa/post/post-comment/delete", method = RequestMethod.POST)
    public String postCommentDelete(@RequestParam(name = "ids", required = false)List<Long> idList,
                                    @RequestParam(name = "page", required = false, defaultValue = "0")final int page) {
        if (idList != null) {
            postCommentService.updateStatusByIdList(Status.DELETE, idList);
        }

        return "redirect:/dfa/post/post-comment?page=" + page;
    }
}
