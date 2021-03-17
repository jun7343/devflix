package kr.devflix.admin.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.entity.DevBlog;
import kr.devflix.service.DevBlogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@Secured(RoleType.MANAGER)
@RequiredArgsConstructor
public class AdminDevBlogController {

    private final DevBlogService devBlogService;

    @RequestMapping(path = "/dfa/dev-post/blog-list", method = RequestMethod.GET)
    public String devBlogList(Model model) {
        List<DevBlog> findAll = devBlogService.findAll();

        model.addAttribute("list", findAll);

        return "/admin/dev-post/blog-list";
    }

    @RequestMapping(path = "/dfa/dev-post/blog-list/{id}", method = RequestMethod.GET)
    public String devBlogDetailForm(@PathVariable(name = "id")final long id, Model model) {
        Optional<DevBlog> findOne = devBlogService.findOneById(id);

        if (findOne.isPresent()) {
            model.addAttribute("item", findOne.get());

            return "/admin/dev-post/blog-detail";
        } else {
            return "redirect:/dfa/dev-post/blog-list";
        }
    }

    @RequestMapping(path = "/dfa/dev-post/blog-list/{id}", method = RequestMethod.POST)
    public String devBlogDetailAction(@PathVariable(name = "id")final long id,
                                      @RequestParam(name = "blog-name", required = false)final String blogName,
                                      @RequestParam(name = "url", required = false)final String URL,
                                      @RequestParam(name = "thumbnail", required = false)final String thumbnail,
                                      @RequestParam(name = "category", required = false)final String category,
                                      @RequestParam(name = "status", required = false) Status status, RedirectAttributes attrs) {
        Optional<DevBlog> findOne = devBlogService.findOneById(id);

        if (findOne.isPresent()) {
            if (StringUtils.isBlank(blogName)) {
                attrs.addFlashAttribute("errorMessage", "블로그 이름 기입해 주세요.");

                return "redirect:/dfa/dev-post/blog-list/" + id;
            } else if (StringUtils.isBlank(URL)) {
                attrs.addFlashAttribute("errorMessage", "URL 기입해 주세요.");

                return "redirect:/dfa/dev-post/blog-list/" + id;
            } else if (StringUtils.isBlank(thumbnail)) {
                attrs.addFlashAttribute("errorMessage", "섬네일 기입해 주세요.");

                return "redirect:/dfa/dev-post/blog-list/" + id;
            } else if (StringUtils.isBlank(category)) {
                attrs.addFlashAttribute("errorMessage", "카테고리 기입해 주세요.");

                return "redirect:/dfa/dev-post/blog-list/" + id;
            } else if (status == null) {
                attrs.addFlashAttribute("errorMessage", "상태 선택해 주세요.");

                return "redirect:/dfa/dev-post/blog-list/" + id;
            }

            devBlogService.updateDevBlog(DevBlog.builder()
                    .id(id)
                    .blogName(blogName)
                    .url(URL)
                    .thumbnail(thumbnail)
                    .status(status)
                    .category(category)
                    .createAt(findOne.get().getCreateAt())
                    .updateAt(new Date())
                    .build());

            attrs.addFlashAttribute("successMessage", "성공적으로 수정 하였습니다.");

            return "redirect:/dfa/dev-post/blog-list/" + id;
        } else {
            return "/admin/dev-post/blog-list";
        }
    }
}
