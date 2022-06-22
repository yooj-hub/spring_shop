package com.yoojshop.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoojshop.api.domain.Post;
import com.yoojshop.api.repository.PostRepository;
import com.yoojshop.api.request.PostCreate;
import com.yoojshop.api.request.PostEdit;
import com.yoojshop.api.request.PostSearch;
import com.yoojshop.api.response.PostResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mockMvc;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다\", \"content\" : \"내용입니다.\"}"))
//                        .param("title", "글 제목입니다.")
//                        .param("content", "글 내용입니다."))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());


    }

    @Test
    @DisplayName("/posts title 값은 필수다.")
    void test2() throws Exception {
        //given

        PostCreate request = PostCreate.builder()
                .title("")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
//                        .param("title", "글 제목입니다.")
//                        .param("content", "글 내용입니다."))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());


    }

    @Test
    @DisplayName("/posts 요청시 db에 값이 저장된다.")
    void test3() throws Exception {

        //given

        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
//                        .param("title", "글 제목입니다.")
//                        .param("content", "글 내용입니다."))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        assertThat(postRepository.count()).isEqualTo(1L);
        Post findPost = postRepository.findAll().get(0);
        assertThat(findPost.getTitle()).isEqualTo("제목입니다.");
        assertThat(findPost.getContent()).isEqualTo("내용입니다.");

    }

    @Test
    @DisplayName("글 1개 조회")
    void 글_1개_조회() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath(("$.title")).value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }

    @Test
    void 제목은_10글자_이하이다() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath(("$.title")).value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }


    @Test
    @DisplayName("글 여러개 조회")
    void 글_여러개_조회() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(post1);
        Post post2 = Post.builder()
                .title("foo2")
                .content("bar2")
                .build();
        postRepository.save(post2);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post2.getId()))
                .andExpect(jsonPath(("$[0].title")).value("foo2"))
                .andExpect(jsonPath("$[0].content").value("bar2"))
                .andExpect(jsonPath("$[1].id").value(post1.getId()))
                .andExpect(jsonPath(("$[1].title")).value("foo1"))
                .andExpect(jsonPath("$[1].content").value("bar1"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 1페이지 조회")
    void 글_1페이지_조회() throws Exception {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("내 제목" + i)
                        .content("Content (shop) - " + i)
                        .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);


        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(29).getId()))
                .andExpect(jsonPath("$[0].title").value("내 제목30"))
                .andExpect(jsonPath("$[0].content").value("Content (shop) - 30"))
                .andDo(print());
    }


    @Test
    @DisplayName("글 제목 수정")
    void 글_제목_수정() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);
        //expected
        PostEdit postEdit = PostEdit.builder()
                .title("yoo")
                .content("bar")
                .build();
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());



    }

}