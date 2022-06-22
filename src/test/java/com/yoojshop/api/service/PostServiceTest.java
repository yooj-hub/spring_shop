package com.yoojshop.api.service;

import com.yoojshop.api.domain.Post;
import com.yoojshop.api.repository.PostRepository;
import com.yoojshop.api.request.PostCreate;
import com.yoojshop.api.request.PostEdit;
import com.yoojshop.api.request.PostSearch;
import com.yoojshop.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {


    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("글 작성")
    void 글_작성() {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        //when
        postService.write(postCreate);

        //then

        Post findPost = postRepository.findAll().get(0);
        assertThat(1L).isEqualTo(postRepository.count());
        assertThat(findPost.getContent()).isEqualTo("내용입니다.");
        assertThat(findPost.getTitle()).isEqualTo("제목입니다.");

    }


    @Test
    @DisplayName("글 1개 조회")
    void 글_한개_조회() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);
        //when
        PostResponse response = postService.get(requestPost.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("foo");
        assertThat(response.getContent()).isEqualTo("bar");

    }

    @Test
    @DisplayName("글 1페이지 조회")
    void 글_1페이지_조회() {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("내 제목" + i)
                        .content("Content (shop) - " + i)
                        .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        //when
        List<PostResponse> posts = postService.getList(pageable);

        //then
        assertThat(posts.size()).isEqualTo(5L);
        assertEquals(posts.get(0).getTitle(), "내 제목30");
        assertEquals(posts.get(4).getContent(), "Content (shop) - 26");
    }


    @Test
    @DisplayName("글 1페이지 조회")
    void 글_1페이지_조회_queryDsl() {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("내 제목" + i)
                        .content("Content (shop) - " + i)
                        .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);


        //when


        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertThat(posts.size()).isEqualTo(10L);
        assertEquals(posts.get(0).getTitle(), "내 제목30");
        assertEquals(posts.get(4).getContent(), "Content (shop) - 26");
    }


    @Test
    @DisplayName("글 1페이지 조회")
    void 글_제목_수정() {
        //given


        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);
        //when
        PostEdit postEdit = PostEdit.builder()
                .title("yoo")
                .build();
        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. [id] : " + post.getId()));

        assertEquals(changedPost.getTitle(), "yoo");
        assertEquals(changedPost.getContent(), "bar");

    }

    @Test
    @DisplayName("글 1페이지 조회")
    void 글_내용_수정() {
        //given


        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);
        //when
        PostEdit postEdit = PostEdit.builder()
                .content("hello")
                .build();
        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. [id] : " + post.getId()));

        assertEquals(changedPost.getTitle(), "foo");
        assertEquals(changedPost.getContent(), "hello");

    }


}