package com.devflix.controller;

import com.devflix.constant.Status;
import com.devflix.constant.RoleType;
import com.devflix.entity.DevPost;
import com.devflix.entity.Member;
import com.devflix.entity.Post;
import com.devflix.entity.PostComment;
import com.devflix.service.DevPostService;
import com.devflix.service.PostCommentService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class APIController {

    private final DevPostService devPostService;
    private final PostCommentService postCommentService;
    private final String IMAGE_ROOT_DIR_PATH;
    private final int DEFAULT_COMMENT_PAGE_PER_SIZE = 10;
    private final SimpleDateFormat commentDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final String DEFAULT_USER_PROFILE_IMG_PATH = "/assets/img/user.jpg";

    public APIController(DevPostService devPostService, PostCommentService postCommentService, Environment environment) {
        this.devPostService = devPostService;
        this.postCommentService = postCommentService;

        if (StringUtils.isBlank(environment.getProperty("image.root-drectory"))) {
            IMAGE_ROOT_DIR_PATH = "images/";
        } else {
            IMAGE_ROOT_DIR_PATH = environment.getProperty("image.root-drectory");
        }
    }

    @RequestMapping(path = "/a/view-count", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionViewCountUpdate(@RequestParam(name = "url", required = false)final String url) {
        if (StringUtils.isBlank(url)) {
            return ImmutableMap.of("result", false);
        }

        final DevPost post = devPostService.updateViewCount(url);

        if (post != null) {
            return ImmutableMap.of("result", true);
        } else {
            return ImmutableMap.of("result", false);
        }
    }

    @RequestMapping(path = "/a/search", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> actionSearchDevPost(@RequestParam(name = "content", required = false)final String content) {
        final String RESULT = "result";
        final String RESULT_DATA = "data";

        if (StringUtils.isBlank(content)) {
            return ImmutableMap.of(RESULT, false);
        }

        List<DevPost> findAll = devPostService.findAllBySearchContentAndStatus(content, Status.POST);
        List<Map<String, String>> dataList = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        for (DevPost post : findAll) {
            Map<String, String> map = new HashMap<>();

            map.put("title", post.getTitle());
            map.put("thumbnail", post.getThumbnail());
            map.put("uploadAt", dateFormat.format(post.getUploadAt()));
            map.put("url", post.getUrl());
            map.put("category", post.getCategory());
            map.put("postType", post.getPostType().name());

            dataList.add(map);
        }

        return ImmutableMap.of(RESULT, true, RESULT_DATA, dataList);
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

        if (! file.exists()) {
            file.mkdirs();
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

        if (page >= 9999) {
            long totalCount = postCommentService.getCountAllByPostIdAndStatus(id, Status.POST);

            findAll = postCommentService.findAllByPostIdAndStatusAndPageRequest(id, Status.POST, (int) (totalCount / DEFAULT_COMMENT_PAGE_PER_SIZE), DEFAULT_COMMENT_PAGE_PER_SIZE);
        } else {
            findAll = postCommentService.findAllByPostIdAndStatusAndPageRequest(id, Status.POST, page, DEFAULT_COMMENT_PAGE_PER_SIZE);
        }

        List<PostComment> content = findAll.getContent();
        List<ImmutableMap<String, Object>> commentList = new ArrayList<>();

        for (PostComment comment : content) {
            commentList.add(ImmutableMap.<String, Object>builder()
                    .put("writer", comment.getWriter().getUsername())
                    .put("userImg", comment.getWriter().getPathBasae() == null? DEFAULT_USER_PROFILE_IMG_PATH : comment.getWriter().getImages().size() == 0? DEFAULT_USER_PROFILE_IMG_PATH
                            : "/images/" + comment.getWriter().getPathBasae() + comment.getWriter().getImages().get(comment.getWriter().getImages().size() - 1))
                    .put("uploadAt", commentDateFormat.format(comment.getCreateAt()))
                    .put("comment", StringEscapeUtils.unescapeHtml4(comment.getComment()))
                    .put("owner", user != null && comment.getWriter().getId().equals(user.getId()))
                    .build());
        }

        Map<String, Object> paging = new HashMap<>();
        List<Integer> pageNumList = new ArrayList<>();

        if (findAll.getTotalPages() > 1) {
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
}
