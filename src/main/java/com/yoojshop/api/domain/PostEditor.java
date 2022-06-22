package com.yoojshop.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostEditor {

    private String title;
    private String content;

}
