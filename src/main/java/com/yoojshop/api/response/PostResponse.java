package com.yoojshop.api.response;

import com.yoojshop.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(Post post){
        id = post.getId();
        title = post.getTitle().substring(0, Math.min(10, post.getTitle().length()));
        content = post.getContent();
    }


    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(10, title.length()));
        this.content = content;
    }
}
