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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

//    @Test
//    @DisplayName("/posts ????????? Hello World??? ????????????.")
//    void test() throws Exception {
//        // expected
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(APPLICATION_JSON)
//                        .content("{\"title\": \"???????????????\", \"content\" : \"???????????????.\"}"))
////                        .param("title", "??? ???????????????.")
////                        .param("content", "??? ???????????????."))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//
//    }

    @Test
    @DisplayName("/posts title ?????? ?????????.")
    void test2() throws Exception {
        //given

        PostCreate request = PostCreate.builder()
                .title("")
                .content("???????????????.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
//                        .param("title", "??? ???????????????.")
//                        .param("content", "??? ???????????????."))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("????????? ???????????????."))
                .andExpect(jsonPath("$.validation.title").value("???????????? ??????????????????."))
                .andDo(print());


    }

    @Test
    @DisplayName("/posts ????????? db??? ?????? ????????????.")
    void test3() throws Exception {

        //given

        PostCreate request = PostCreate.builder()
                .title("???????????????.")
                .content("???????????????.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
//                        .param("title", "??? ???????????????.")
//                        .param("content", "??? ???????????????."))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        assertThat(postRepository.count()).isEqualTo(1L);
        Post findPost = postRepository.findAll().get(0);
        assertThat(findPost.getTitle()).isEqualTo("???????????????.");
        assertThat(findPost.getContent()).isEqualTo("???????????????.");

    }

    @Test
    @DisplayName("??? 1??? ??????")
    void ???_1???_??????() throws Exception {
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
    void ?????????_10??????_????????????() throws Exception {
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
    @DisplayName("??? ????????? ??????")
    void ???_?????????_??????() throws Exception {
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
    @DisplayName("??? 1????????? ??????")
    void ???_1?????????_??????() throws Exception {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("??? ??????" + i)
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
                .andExpect(jsonPath("$[0].title").value("??? ??????30"))
                .andExpect(jsonPath("$[0].content").value("Content (shop) - 30"))
                .andDo(print());
    }


    @Test
    @DisplayName("??? ?????? ??????")
    void ???_??????_??????() throws Exception {
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

    @Test
    @DisplayName("????????? ??????")
    void ?????????_??????() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);

        //expected

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(postRepository.count()).isEqualTo(0);

    }


    @Test
    @DisplayName("???????????? ?????? ????????? ??????")
    void ????????????_??????_?????????_??????() throws Exception {
        //given
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

        //when

        //then
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ??????")
    void ????????????_??????_?????????_??????() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("yoo")
                .content("bar")
                .build();



        //expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("??????????????? ????????? ?????????????????????")
    void ???????????????_?????????_?????????????????????() throws Exception {

        //given

        PostCreate request = PostCreate.builder()
                .title("??????123123")
                .content("???????????????.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isBadRequest())
                .andDo(print());


    }


}