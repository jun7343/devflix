package kr.devflix.admin.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.entity.Post;
import kr.devflix.service.PostService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;
    private final int DEFAULT_PER_PAGE_SIZE = 20;

    @RequestMapping(path = "/dfa/post/post-list", method = RequestMethod.GET)
    public String postList(@RequestParam(name = "page", required = false, defaultValue = "0")final int page,
                                @RequestParam(name = "title", required = false)final String title,
                                @RequestParam(name = "writer", required = false)final String writer,
                                @RequestParam(name = "status", required = false)final Status status,
                                Model model) {
        Page<Post> findList = null;

        if (StringUtils.isBlank(title) && StringUtils.isBlank(writer) && status == null) {
            findList = postService.findAll(page, DEFAULT_PER_PAGE_SIZE);
        } else {
            findList = postService.findAllBySearch(title, writer, status, page, DEFAULT_PER_PAGE_SIZE);
        }

        model.addAttribute("search-title", title);
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

        return "/admin/post/post-list";
    }

    @RequestMapping(path = "/dfa/post/post-list/delete", method = RequestMethod.POST)
    public String postListDelete(@RequestParam(name = "ids", required = false)List<Long> idList,
                                       @RequestParam(name = "page", required = false, defaultValue = "0")int page) {
        if (idList != null) {
            postService.updateStatusByIdList(Status.DELETE, idList);
        }

        return "redirect:/dfa/post/post-list?page=" + page;
    }

    @RequestMapping(path = "/dfa/post/post-list/hidden", method = RequestMethod.POST)
    public String postListHidden(@RequestParam(name = "ids", required = false)List<Long> idList,
                                 @RequestParam(name = "page", required = false, defaultValue = "0")int page) {
        if (idList != null) {
            postService.updateStatusByIdList(Status.HIDDEN, idList);
        }

        return "redirect:/dfa/post/post-list?page=" + page;
    }

    @RequestMapping(path = "/dfa/post/post-list/post", method = RequestMethod.POST)
    public String postListPost(@RequestParam(name = "ids", required = false)List<Long> idList,
                                 @RequestParam(name = "page", required = false, defaultValue = "0")int page) {
        if (idList != null) {
            postService.updateStatusByIdList(Status.POST, idList);
        }

        return "redirect:/dfa/post/post-list?page=" + page;
    }
}
