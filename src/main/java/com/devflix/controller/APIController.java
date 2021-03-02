package com.devflix.controller;

import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class APIController {

    private final DevPostService devPostService;

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
}
