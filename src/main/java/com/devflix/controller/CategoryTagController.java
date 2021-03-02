package com.devflix.controller;

import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryTagController {

    private final DevPostService devPostService;
    private final int DEFAULT_PAGE_PER_SIZE = 20;

    @RequestMapping(path = "/{type}/{parameter}", method = RequestMethod.GET)
    public String index(@PathVariable(name = "type")final String type, @PathVariable(name = "parameter")final String parameter,
                        @RequestParam(name = "page", defaultValue = "0")int page, Model model, HttpServletResponse response){
        Page<DevPost> findList = null;
        List<Integer> pageNumList = new ArrayList<>();

        if (StringUtils.equals(type, "category")) {
            findList = devPostService.findAllByCategoryOrderByUploadAt(parameter, page, DEFAULT_PAGE_PER_SIZE);
        } else if (StringUtils.equals(type, "tag")) {
            findList = devPostService.findAllByTagOrderByUploadAt(parameter, page, DEFAULT_PAGE_PER_SIZE);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        if (findList != null) {
            model.addAttribute("list", findList);
            model.addAttribute("previousPage", findList.getNumber() / 5 != 0);

            if (findList.getNumber() / 5 != 0 && ((findList.getNumber() / 5) * 5 - 1) > 0) {
                model.addAttribute("previousPageNum", (findList.getNumber() / 5) * 5 - 1);
            }

            model.addAttribute("nextPage", (findList.getNumber() / 5) * 5 + 6 <= findList.getTotalPages());

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

            model.addAttribute("type", type);
            model.addAttribute("parameter", parameter);
        }

        return "category-tag";
    }
}
