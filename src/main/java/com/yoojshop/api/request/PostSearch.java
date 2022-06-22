package com.yoojshop.api.request;


import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearch {

    private static final int MAX_SIZE = 2000;


    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    public long getOffset(){
        return ((long) (Math.max((page-1),0) )* Math.min(size, MAX_SIZE));
    }



}
