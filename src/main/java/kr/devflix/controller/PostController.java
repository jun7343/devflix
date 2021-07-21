package kr.devflix.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.dto.PostDto;
import kr.devflix.entity.DevPost;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.service.DevPostService;
import kr.devflix.service.PostCommentAlertService;
import kr.devflix.service.PostService;
import kr.devflix.utils.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.query.JpaQueryExecution;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final DevPostService devPostService;
    private final PostCommentAlertService postCommentAlertService;

    public PostController(PostService postService, DevPostService devPostService, PostCommentAlertService postCommentAlertService) {
        this.postService = postService;
        this.devPostService = devPostService;
        this.postCommentAlertService = postCommentAlertService;
    }

    @GetMapping
    public String list(@RequestParam(required = false, defaultValue = "0")final int page,
                       @RequestParam(name = "per-page", required = false, defaultValue = "20")final int perPage,
                       @RequestParam(required = false)final String search, Model model) {
        Page<PostDto> findAll = postService.getAllByStatusOrSearch(Status.POST, search, page, perPage);

        model.addAttribute("list", findAll.getContent());
        model.addAttribute("pagination", Pagination.builder(findAll.getTotalElements())
                .currentPage(findAll.getNumber())
                .size(findAll.getSize())
                .build());
        model.addAttribute("search", search);

        return "/posts/list";
    }

    @Secured(RoleType.USER)
    @GetMapping("/write")
    public String writeForm() {
        return "/posts/write";
    }

    @Secured(RoleType.USER)
    @PostMapping("/write")
    public String writeAction(@ModelAttribute PostDto postDto, @AuthenticationPrincipal Member member) throws Exception {
        Post save = postService.createPost(postDto, Status.POST, member);

        if (save == null) {
            throw new Exception("");
        }

        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String readForm(@PathVariable(name = "id")long id, HttpServletRequest request,
                           @RequestParam(name = "ca", required = false)final Long ca, Model model, @AuthenticationPrincipal Member user) {
        Optional<Post> postItem = postService.findOneById(id);

        if (postItem.isPresent() && postItem.get().getStatus() == Status.POST) {
            Post post = postItem.get();

            model.addAttribute("item", post);
            model.addAttribute("siteUrl", request.getRequestURL());
            model.addAttribute("postOwner", user != null && post.getWriter().getId().equals(user.getId()));

            if (ca != null && user != null) {
                postCommentAlertService.updateAllConfirmByPostAndUser(post, user);
            }

            if (! StringUtils.isBlank(post.getDevPostUrl())) {
                Optional<DevPost> findDevPost = devPostService.findOneByUrlAndStatus(post.getDevPostUrl(), Status.POST);

                findDevPost.ifPresent(devPost -> model.addAttribute("devPost", devPost));
            }

            return "/post/read";
        } else {
            model.addAttribute("errorMessage", "해당 post가 없습니다.");

            return "redirect:/post";
        }
    }

    @Secured(RoleType.USER)
    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable(name = "id")final long id, Model model, @AuthenticationPrincipal Member user) {
        Optional<Post> postItem = postService.findOneById(id);

        if (postItem.isPresent() && postItem.get().getWriter().getId().equals(user.getId()) && postItem.get().getStatus() == Status.POST) {
            model.addAttribute("item", postItem.get());

            if (! StringUtils.isBlank(postItem.get().getDevPostUrl())) {
                Optional<DevPost> findDevPost = devPostService.findOneByUrlAndStatus(postItem.get().getDevPostUrl(), Status.POST);

                findDevPost.ifPresent(devPost -> model.addAttribute("devPost", devPost));
            }

            return "/post/modify";
        } else {
            return "redirect:/post";
        }
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/post/modify/{id}", method = RequestMethod.POST)
    public String modifyAction(PostDto postDto, RedirectAttributes attrs) {


        return "redirect:/post";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/post/delete/{id}", method = RequestMethod.GET)
    public String deleteAction(@PathVariable(name = "id")final long id, @AuthenticationPrincipal Member user) {
        Optional<Post> item = postService.findOneById(id);


        return "redirect:/post";
    }
}
