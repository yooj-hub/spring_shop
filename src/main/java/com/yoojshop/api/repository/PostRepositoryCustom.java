package com.yoojshop.api.repository;

import com.yoojshop.api.domain.Post;
import com.yoojshop.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
