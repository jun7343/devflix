package kr.devflix.posts;

import kr.devflix.DevflixApplication;
import kr.devflix.constant.PostType;
import kr.devflix.constant.Status;
import kr.devflix.controller.DevPostRestController;
import kr.devflix.entity.DevPost;
import kr.devflix.repository.DevPostRepository;
import kr.devflix.service.DevPostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DevflixApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("local")
class DevPostRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DevPostRestController devPostRestController;

    @Autowired
    private DevPostService devPostService;

    @Autowired
    private DevPostRepository devPostRepository;

    private static final List<DevPost> devPostList = new ArrayList<>();

    DevPostRestControllerTest() {
        devPostList.add(DevPost.builder()
                .id(1L)
                .category("KAKAO")
                .postType(PostType.YOUTUBE)
                .status(Status.POST)
                .title("KAKAO title")
                .description("KAKAO content")
                .writer("KAKAO")
                .url("https://")
                .thumbnail("https://")
                .view(0)
                .uploadAt(new Date())
                .createAt(new Date())
                .updateAt(new Date())
                .build()
        );
    }

    @Test
    @DisplayName("Dev Post List 조회")
    void devPostList() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/a/dev-posts")
                        .param("c", "KAKAO")
                        .param("page", "0")
                        .param("per-page", "20")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(handler().methodName("actionDevPostList"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Dev Post view count 업데이트")
    void updatePostViewCount() throws Exception {
        final DevPost post = DevPost.builder()
                .id(1L)
                .category("KAKAO")
                .postType(PostType.BLOG)
                .view(0)
                .tags(new ArrayList<>())
                .description("KAKAO Description")
                .thumbnail("http://")
                .url("http://www.test.com")
                .writer("KAKAO Company")
                .createAt(new Date())
                .updateAt(new Date())
                .uploadAt(new Date())
                .build();

        ResultActions result = mockMvc.perform(
                patch("/a/dev-posts")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(DevPostRestController.class))
                .andExpect(handler().methodName("actionDevPostViewCountUpdate"));
    }
}