package com.devflix.controller;

import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
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
    private final String IMAGE_ROOT_DIR_PATH;

    public APIController(DevPostService devPostService, Environment environment) {
        this.devPostService = devPostService;

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

        List<DevPost> findAll = devPostService.findAllBySearchContent(content);
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
                System.out.println("false!!");
                return ImmutableMap.of("result", false);
            }
        } else {
            System.out.println("what!?");
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
}
