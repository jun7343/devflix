package kr.devflix.controller;

import kr.devflix.constant.MemberStatus;
import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.dto.PostDto;
import kr.devflix.entity.DevPost;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.service.DevPostService;
import kr.devflix.service.PostCommentAlertService;
import kr.devflix.service.PostCommentService;
import kr.devflix.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final int DEFAULT_SIZE_VALUE = 20;
    private final PostService postService;
    private final DevPostService devPostService;
    private final PostCommentService postCommentService;
    private final PostCommentAlertService postCommentAlertService;

    @RequestMapping(path = "/post", method = RequestMethod.GET)
    public String list(@RequestParam(name = "page", required = false, defaultValue = "0")int page,
                       @RequestParam(name = "search", required = false)final String search, Model model) {
        Page<Post> findList = null;

        if (StringUtils.isBlank(search)) {
             findList = postService.findAllByStatusAndWriterStatusAndPageRequest(Status.POST, MemberStatus.ACTIVE, page, DEFAULT_SIZE_VALUE);
        } else {
            findList = postService.findAllBySearchAndStatusAndWrtierStatusAndPageRequest(search, Status.POST, MemberStatus.ACTIVE, page, DEFAULT_SIZE_VALUE);

            model.addAttribute("search", search);
        }

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

        return "/post/list";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/post/write", method = RequestMethod.GET)
    public String writeForm() {
        return "/post/write";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/post/write", method = RequestMethod.POST)
    public String writeAction(@RequestParam(name = "title", required = false)final String title, @RequestParam(name = "post-url", required = false)final String devPostUrl,
                              @RequestParam(name = "path-base", required = false)final String pathBase, @RequestParam(name = "images", required = false)List<String> images,
                              @RequestParam(name = "content", required = false)final String content, @AuthenticationPrincipal Member writer,
                              RedirectAttributes attrs) {
        if (StringUtils.isBlank(title)) {
            attrs.addFlashAttribute("errorMessage", "제목 기입해 주세요.");

            return "redirect:/post/write";
        }

        PostDto dto = PostDto.builder()
                .status(Status.POST)
                .title(title)
                .content(content)
                .writer(writer)
                .pathBase(pathBase)
                .images(images == null? new ArrayList<>() : images)
                .devPostUrl(devPostUrl)
                .view(0)
                .build();

        postService.createPost(dto);

        return "redirect:/post";
    }

    @RequestMapping(path = "/post/read/{id}", method = RequestMethod.GET)
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
    @RequestMapping(path = "/post/modify/{id}", method = RequestMethod.GET)
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
    public String modifyAction(@PathVariable(name = "id")final long id, @RequestParam(name = "title", required = false)final String title, @RequestParam(name = "post-url", required = false)final String devPostUrl,
                               @RequestParam(name = "path-base", required = false)final String pathBase, @RequestParam(name = "images", required = false)List<String> images,
                               @RequestParam(name = "content", required = false)final String content, @AuthenticationPrincipal Member user,
                               RedirectAttributes attrs) {
        if (StringUtils.isBlank(title)) {
            attrs.addFlashAttribute("errorMessage", "제목 기입해 주세요.");

            return "redirect:/post/write";
        }

        Optional<Post> postItem = postService.findOneById(id);

        if (postItem.isPresent() && postItem.get().getStatus() == Status.POST && postItem.get().getWriter().getId().equals(user.getId())) {
            PostDto dto = PostDto.builder()
                    .id(postItem.get().getId())
                    .status(Status.POST)
                    .title(title)
                    .content(content)
                    .writer(postItem.get().getWriter())
                    .pathBase(pathBase)
                    .images(images == null? new ArrayList<>() : images)
                    .devPostUrl(devPostUrl)
                    .view(postItem.get().getView())
                    .createAt(postItem.get().getCreateAt())
                    .updateAt(new Date())
                    .build();

            System.out.println(dto.toString());

            postService.updatePost(dto);
        }

        return "redirect:/post";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/post/delete/{id}", method = RequestMethod.GET)
    public String deleteAction(@PathVariable(name = "id")final long id, @AuthenticationPrincipal Member user) {
        Optional<Post> item = postService.findOneById(id);

        if (item.isPresent()) {
            Post post = item.get();

            if (post.getStatus() == Status.POST && post.getWriter().getId().equals(user.getId())) {
                postService.updatePost(PostDto.builder()
                        .id(post.getId())
                        .status(Status.DELETE)
                        .title(post.getTitle())
                        .content(post.getContent())
                        .writer(post.getWriter())
                        .pathBase(post.getPathBase())
                        .images(post.getImages())
                        .devPostUrl(post.getDevPostUrl())
                        .view(post.getView())
                        .createAt(post.getCreateAt())
                        .updateAt(new Date())
                        .build());

                return "redirect:/post";
            }
        }

        return "redirect:/post";
    }
}
