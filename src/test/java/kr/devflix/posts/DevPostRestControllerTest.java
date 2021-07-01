package kr.devflix.posts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class DevPostRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevPostService devPostService;

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
        final DevPost post = new DevPost().builder()
                .id(1L)
                .category("KAKAO")
                .postType(PostType.BLOG)
                .view(0)
                .tags(new ArrayList<>())
                .description("KAKAO Description")
                .thumbnail("http://")
                .url("http://")
                .writer("KAKAO Company")
                .createAt(new Date())
                .updateAt(new Date())
                .uploadAt(new Date())
                .build();

        given(devPostService.createDevPost(post)).willReturn(post);

        ResultActions result = mockMvc.perform(
                patch("/a/dev-posts/view/1")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(DevPostRestController.class))
                .andExpect(handler().methodName("actionDevPostViewCountUpdate"));
    }
}