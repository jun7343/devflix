package kr.devflix.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.entity.*;
import kr.devflix.entity.DevPost;
import kr.devflix.service.DevPostService;
import kr.devflix.service.*;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class APIController {

    private final DevPostService devPostService;
    private final PostCommentService postCommentService;
    private final PostService postService;
    private final DevBlogService devBlogService;
    private final YoutubeChannelService youtubeChannelService;
    private final String IMAGE_ROOT_DIR_PATH;
    private final int DEFAULT_COMMENT_PAGE_PER_SIZE = 10;
    private final int DEFAULT_DEV_POST_PAGE_PER_SIZE = 20;
    private final SimpleDateFormat commentDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final String DEFAULT_USER_PROFILE_IMG_PATH = "/assets/img/user.jpg";
    private final SimpleDateFormat devPostDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
    private final Logger logger = LoggerFactory.getLogger(APIController.class);

    public APIController(final DevPostService devPostService, final PostCommentService postCommentService, final PostService postService,
                         final DevBlogService devBlogService, final YoutubeChannelService youtubeChannelService, final Environment environment) {
        this.devPostService = devPostService;
        this.postCommentService = postCommentService;
        this.postService = postService;
        this.devBlogService = devBlogService;
        this.youtubeChannelService = youtubeChannelService;

        if (StringUtils.isBlank(environment.getProperty("image.root-drectory"))) {
            IMAGE_ROOT_DIR_PATH = "images/";
        } else {
            IMAGE_ROOT_DIR_PATH = environment.getProperty("image.root-drectory");
        }
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/a/image-upload", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionImageUpload(@RequestParam(name = "images")MultipartFile[] images,
                                                          @RequestParam(name = "path-base", required = false)String pathBase) {
        if (StringUtils.isBlank(pathBase)) {
            pathBase = getPathBase();
        }

        List<Object> list = new LinkedList<>();

        for (MultipartFile img : images) {
            if (StringUtils.equals(img.getContentType(), MediaType.IMAGE_GIF_VALUE) || StringUtils.equals(img.getContentType(), MediaType.IMAGE_JPEG_VALUE) ||
                    StringUtils.equals(img.getContentType(), MediaType.IMAGE_PNG_VALUE)) {
                StringBuilder imageName = new StringBuilder();

                imageName.append(System.currentTimeMillis()).append(".").append(Objects.requireNonNull(img.getContentType()).split("/")[1]);

                try {
                    File file = new File(IMAGE_ROOT_DIR_PATH + pathBase + imageName.toString());

                    img.transferTo(Paths.get(file.getPath()));

                    ImmutableMap<String, Object> map = ImmutableMap.<String, Object>builder()
                            .put("pathBase", pathBase)
                            .put("imageName", imageName.toString())
                            .put("imageOriginName", img.getOriginalFilename())
                            .put("imageURL", "/images/" + pathBase + imageName.toString())
                            .build();

                    list.add(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ImmutableMap.of("result", list);
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/a/image-delete", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionImageDelete(@RequestParam(name = "pathBase", required = false)final String pathBase,
                                                          @RequestParam(name = "imageName", required = false)final String imageName) {
        if (StringUtils.isBlank(pathBase) || StringUtils.isBlank(imageName)) {
            return ImmutableMap.of("result", false);
        }

        File deleteFile = new File(IMAGE_ROOT_DIR_PATH + pathBase + imageName);

        if (deleteFile.exists()) {
            if (deleteFile.delete()) {
                return ImmutableMap.of("result", true);
            } else {
                return ImmutableMap.of("result", false);
            }
        } else {
            return ImmutableMap.of("result", false);
        }
    }

    private String getPathBase() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder builder = new StringBuilder();

        builder.append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1).append("/")
                .append(calendar.get(Calendar.DATE)).append("/").append(calendar.getTimeInMillis()).append("/");

        File file = new File(IMAGE_ROOT_DIR_PATH + builder.toString());

        if (file.mkdirs()) {
            logger.debug("path base directory create done!!");
        } else{
            logger.error("path base directory create error!!");
        }

        return builder.toString();
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
