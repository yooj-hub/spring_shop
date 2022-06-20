package com.yoojshop.api.controller;

import com.yoojshop.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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




    // 데이터를 검증하는 이유
    // 1. client 개발자가 깜박할 수 있다. 실수로 값을 안보낼 수 있다.
    // 2. client bug로 값이 누락될 수 있다.
    // 3. 외부에 나쁜 사람이 값을 임의로 좆가해서 보낼 수 있다.
    // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
    // 5. 서버 개발자의 편안함을 위해서
//    @PostMapping("/posts")
//    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) throws Exception {
//        if(result.hasErrors()){
//            Map<String, String> error = new HashMap<>();
//            for (FieldError fieldError : result.getFieldErrors()) {
//                error.put(fieldError.getField(), fieldError.getDefaultMessage());
//            }
//            return error;
//        }
//
//        log.info("params = {}", params.toString());
//        return Map.of();
//    }

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params) throws Exception {

        return Map.of();
    }


}