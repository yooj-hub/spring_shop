package com.yoojshop.api.service;

import com.yoojshop.api.domain.Post;
import com.yoojshop.api.domain.PostEditor;
import com.yoojshop.api.exception.PostNotFound;
import com.yoojshop.api.repository.PostRepository;
import com.yoojshop.api.request.PostCreate;
import com.yoojshop.api.request.PostEdit;
import com.yoojshop.api.request.PostSearch;
import com.yoojshop.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // postCreate -> Entity


        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        return new PostResponse(postRepository.findById(id).orElseThrow(PostNotFound::new));


    }

    public List<PostResponse> getList(Pageable pageable) {
        return postRepository.findAll(pageable).stream().map(PostResponse::new).collect(Collectors.toList());
    }
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream().map(PostResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);


        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
        if(postEdit.getTitle() != null){
            postEditorBuilder.title(postEdit.getTitle());
        }
        if(postEdit.getContent() != null){
            postEditorBuilder.content(postEdit.getContent());
        }
        post.edit(postEditorBuilder.build());

    }

    @Transactional
    public void delete(Long id){
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);

        // 존재하는 경우

        postRepository.delete(post);


    }




}
