package kr.devflix.admin.controller;

import kr.devflix.constant.PostType;
import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.entity.DevPost;
import kr.devflix.service.DevPostService;
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

@Secured(RoleType.MANAGER)
@Controller
@RequiredArgsConstructor
public class AdminDevPostListController {

    private final DevPostService devPostService;
    private final int DEFAULT_PER_PAGE_SIZE = 20;

    @RequestMapping(path = "/dfa/dev-post/list", method = RequestMethod.GET)
    public String devPostList(@RequestParam(name = "page", required = false, defaultValue = "0")final int page,
                              @RequestParam(name = "title", required = false)final String title,
                              @RequestParam(name = "category", required = false)final String category,
                              @RequestParam(name = "type", required = false)final PostType type,
                              @RequestParam(name = "status", required = false) Status status,
                              Model model) {
        Page<DevPost> findList = null;

        if (StringUtils.isBlank(title) && StringUtils.isBlank(category) && type == null && status == null) {
            findList = devPostService.findAll(page, DEFAULT_PER_PAGE_SIZE);
        } else {
            findList = devPostService.findAllBySearch(title, category, type, status, page, DEFAULT_PER_PAGE_SIZE);
        }

        model.addAttribute("search-title", title);
        model.addAttribute("search-category", category);
        model.addAttribute("search-type", type);
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

        return "/admin/dev-post/list";
    }

    @RequestMapping(path = "/dfa/dev-post/list/post", method = RequestMethod.POST)
    public String devPostPost(@RequestParam(name = "ids", required = false)List<Long> idList,
                              @RequestParam(name = "page", required = false, defaultValue = "0")final int page) {
        if (idList != null) {
            devPostService.updateStatusByIdList(Status.POST, idList);
        }

        return "redirect:/dfa/dev-post/list?page=" + page;
    }

    @RequestMapping(path = "/dfa/dev-post/list/hidden", method = RequestMethod.POST)
    public String devPostHidden(@RequestParam(name = "ids", required = false)List<Long> idList,
                              @RequestParam(name = "page", required = false, defaultValue = "0")final int page) {
        if (idList != null) {
            devPostService.updateStatusByIdList(Status.HIDDEN, idList);
        }

        return "redirect:/dfa/dev-post/list?page=" + page;
    }

    @RequestMapping(path = "/dfa/dev-post/list/delete", method = RequestMethod.POST)
    public String devPostDelete(@RequestParam(name = "ids", required = false)List<Long> idList,
                              @RequestParam(name = "page", required = false, defaultValue = "0")final int page) {
        if (idList != null) {
            devPostService.updateStatusByIdList(Status.DELETE, idList);
        }

        return "redirect:/dfa/dev-post/list?page=" + page;
    }
}
