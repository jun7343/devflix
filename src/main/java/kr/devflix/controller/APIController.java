package kr.devflix.controller;

import com.google.common.collect.ImmutableMap;
import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.entity.*;
import kr.devflix.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class APIController {

    private final DevPostService devPostService;
    private final PostCommentService postCommentService;
    private final PostService postService;
    private final DevBlogService devBlogService;
    private final YoutubeChannelService youtubeChannelService;

    private final int DEFAULT_COMMENT_PAGE_PER_SIZE = 10;
    private final SimpleDateFormat commentDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final String DEFAULT_USER_PROFILE_IMG_PATH = "/assets/img/user.jpg";

    public APIController(final DevPostService devPostService, final PostCommentService postCommentService, final PostService postService,
                         final DevBlogService devBlogService, final YoutubeChannelService youtubeChannelService) {
        this.devPostService = devPostService;
        this.postCommentService = postCommentService;
        this.postService = postService;
        this.devBlogService = devBlogService;
        this.youtubeChannelService = youtubeChannelService;
    }





    @Secured(RoleType.USER)
    @RequestMapping(path = "/a/post-comment-save", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionCommentSave(@RequestParam(name = "post-id")long id,
                                                          @RequestParam(name = "comment")final String comment,
                                                          @AuthenticationPrincipal Member member) {
        PostComment saveComment = postCommentService.createComment(id, comment, member);

        if (saveComment != null) {
            return ImmutableMap.of("result", true);
        } else {
            return ImmutableMap.of("result", false);
        }
    }

    @RequestMapping(path = "/a/post-comment-list", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionGetComment(@RequestParam(name = "post-id")final long id,
                                                         @RequestParam(name = "page", required = false, defaultValue = "0")final int page,
                                                         @AuthenticationPrincipal Member user) {
        Page<PostComment> findAll = null;

        if (page >= 999) {
            long totalCount = postCommentService.getCountAllByPostIdAndStatus(id, Status.POST);
            int p = (int) totalCount / DEFAULT_COMMENT_PAGE_PER_SIZE > 0 && (int) totalCount % DEFAULT_COMMENT_PAGE_PER_SIZE == 0?
                    (int) totalCount / DEFAULT_COMMENT_PAGE_PER_SIZE - 1 : (int) totalCount / DEFAULT_COMMENT_PAGE_PER_SIZE;

            findAll = postCommentService.findAllByPostIdAndStatusAndPageRequest(id, Status.POST, p, DEFAULT_COMMENT_PAGE_PER_SIZE);
        } else {
            findAll = postCommentService.findAllByPostIdAndStatusAndPageRequest(id, Status.POST, page, DEFAULT_COMMENT_PAGE_PER_SIZE);
        }

        List<PostComment> content = findAll.getContent();
        List<ImmutableMap<String, Object>> commentList = new ArrayList<>();

        for (PostComment comment : content) {
            commentList.add(ImmutableMap.<String, Object>builder()
                    .put("id", comment.getId())
                    .put("writer", comment.getWriter().getUsername())
                    .put("userImg", !StringUtils.isBlank(comment.getWriter().getPathBase()) && !StringUtils.isBlank(comment.getWriter().getImagePath())?
                            "/images/" + comment.getWriter().getPathBase() + comment.getWriter().getImagePath() : DEFAULT_USER_PROFILE_IMG_PATH)
                    .put("uploadAt", commentDateFormat.format(comment.getCreateAt()))
                    .put("comment", StringEscapeUtils.unescapeHtml4(comment.getComment()))
                    .put("owner", user != null && comment.getWriter().getId().equals(user.getId()))
                    .build());
        }

        Map<String, Object> paging = new HashMap<>();

        if (findAll.getTotalPages() > 1) {
            List<Integer> pageNumList = new ArrayList<>();

            paging.put("previousPage", findAll.getNumber() / 5 != 0);

            if (findAll.getNumber() / 5 != 0 && ((findAll.getNumber() / 5) * 5 - 1) > 0) {
                paging.put("previousPageNum", (findAll.getNumber() / 5) * 5 - 1);
            }

            paging.put("nextPage", (findAll.getNumber() / 5) * 5 + 6 <= findAll.getTotalPages());

            if ((findAll.getNumber() / 5) * 5 + 6 <= findAll.getTotalPages()) {
                paging.put("nextPageNum", (findAll.getNumber() / 5 + 1) * 5);
            }

            int start = (findAll.getNumber() / 5) * 5 + 1;
            int end = Math.min((findAll.getNumber() / 5 + 1) * 5, findAll.getTotalPages());

            for (int i = start; i <= end; i++) {
                pageNumList.add(i);
            }

            paging.put("pageNumList", pageNumList);
            paging.put("currentPageNum", findAll.getNumber() + 1);
        }

        return ImmutableMap.<String, Object>builder()
                .put("commentList", commentList)
                .put("paging", paging)
                .build();
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/a/post-comment-delete", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionCommentDelete(@RequestParam(name = "id")final long id, @AuthenticationPrincipal Member user) {
        Optional<PostComment> item = postCommentService.findOneById(id);

        if (item.isPresent()) {
            PostComment comment = item.get();

            if (comment.getStatus() == Status.POST && comment.getWriter().getId().equals(user.getId())) {
                postCommentService.updatePostComment(PostComment.builder()
                        .id(comment.getId())
                        .writer(comment.getWriter())
                        .post(comment.getPost())
                        .comment(comment.getComment())
                        .status(Status.DELETE)
                        .createAt(comment.getCreateAt())
                        .updateAt(new Date())
                        .build());

                return ImmutableMap.of("result", true);
            }
        }

        return ImmutableMap.of("result", false);
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/post/comment", method = RequestMethod.GET)
    public String commentModifyForm(@RequestParam(name = "pId")final long pId, @RequestParam(name = "cId")final long cId,
                                    @AuthenticationPrincipal Member user, Model model) {
        final Optional<Post> findPost = postService.findOneById(pId);
        final Optional<PostComment> findComment = postCommentService.findOneById(cId);

        if (findPost.isPresent() && findComment.isPresent()) {
            Post post = findPost.get();
            PostComment comment = findComment.get();

            if (post.getStatus() == Status.POST && comment.getStatus() == Status.POST && comment.getWriter().getId().equals(user.getId())) {
                model.addAttribute("pId", pId);
                model.addAttribute("cId", cId);

                return "/post/comment-modify";
            }
        }

        return "redirect:/post";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/post/comment", method = RequestMethod.POST)
    public String commentModifyAction(@RequestParam(name = "pId")final long pId, @RequestParam(name = "cId")final long cId,
                                    @RequestParam(name = "content", required = false)final String content,
                                    @AuthenticationPrincipal Member user, RedirectAttributes attrs) {
        if (StringUtils.isBlank(content)) {
            attrs.addFlashAttribute("errorMessage", "수정 내용 기입해 주세요.");

            return "redirect:/post/comment?pId=" + pId + "&cId=" + cId;
        }

        final Optional<Post> findPost = postService.findOneById(pId);
        final Optional<PostComment> findComment = postCommentService.findOneById(cId);

        if (findPost.isPresent() && findComment.isPresent()) {
            Post post = findPost.get();
            PostComment comment = findComment.get();

            if (post.getStatus() == Status.POST && comment.getStatus() == Status.POST && comment.getWriter().getId().equals(user.getId())) {
                postCommentService.updatePostComment(PostComment.builder()
                            .id(comment.getId())
                            .comment(content)
                            .writer(comment.getWriter())
                            .status(comment.getStatus())
                            .post(comment.getPost())
                            .createAt(comment.getCreateAt())
                            .updateAt(new Date())
                            .build());

                return "redirect:/post/read/" + pId;
            }
        }

        return "redirect:/post";
    }

    @RequestMapping(path = "/a/all-category", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionGetDevPostByCategory(@RequestParam(name = "type", required = false)final String type) {
        List<ImmutableMap<String, Object>> result = new ArrayList<>();

        if (StringUtils.equalsIgnoreCase("blog", type)) {
            List<DevBlog> findAll = devBlogService.findAllDevBlogByStatus(Status.POST);

            for (DevBlog blog : findAll) {
                result.add(ImmutableMap.<String, Object>builder()
                        .put("category", blog.getCategory())
                        .put("thumbnail", blog.getThumbnail())
                        .build());
            }
        } else if (StringUtils.equalsIgnoreCase("youtube", type)) {
            List<YoutubeChannel> findAll = youtubeChannelService.findAllByStatusOrderByCreateAtDesc(Status.POST);

            for (YoutubeChannel channel : findAll) {
                result.add(ImmutableMap.<String, Object>builder()
                        .put("category", channel.getCategory())
                        .put("thumbnail", channel.getThumbnail())
                        .build());
            }
        }

        return ImmutableMap.of("result", result);
    }
}
