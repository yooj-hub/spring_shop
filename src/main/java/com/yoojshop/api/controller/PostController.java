package com.yoojshop.api.controller;

import com.yoojshop.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class PostController {

    // SSR -> JSP, Thymeleaf mustache freemarker // Server sice rendering

    // SPA ->
    // vue -> vue+SSR = nuxt
    // react -> react+SSR = next

//    @PostMapping("/posts")
//    public String post(@RequestParam String title, @RequestParam String content){
//        log.info("title = {}, content = {}", title, content);
//        return "Hello World";
//    }

    // Map 사용
//    @PostMapping("/posts")
//    public String post(@RequestParam Map<String, String> params){
//        log.info("title = {}, content = {}", params.get("title"), params.get("content"));
//        return "Hello World";
//    }

    // DTO 시용용
    @PostMapping("/posts")
    public String post(@RequestBody PostCreate postCreate){
        log.info("params = {}", postCreate.toString());
        return "Hello World";
    }

}