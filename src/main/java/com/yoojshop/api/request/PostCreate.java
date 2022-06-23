package com.yoojshop.api.request;

import com.yoojshop.api.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostCreate {
    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;
    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;


    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.contains("바보"))
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
    }
}
