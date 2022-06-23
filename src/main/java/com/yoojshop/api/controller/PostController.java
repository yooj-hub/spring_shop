package com.yoojshop.api.controller;

import com.yoojshop.api.domain.Post;
import com.yoojshop.api.exception.InvalidRequest;
import com.yoojshop.api.request.PostCreate;
import com.yoojshop.api.request.PostEdit;
import com.yoojshop.api.request.PostSearch;
import com.yoojshop.api.response.PostResponse;
import com.yoojshop.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        request.validate();


        postService.write(request);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */

    /**
     * 단건 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);

    }

    /**
     * 여러개 글 조회 api
     *
     * @param /posts
     */


    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request){
        log.info("[post id] : {}",postId);
        postService.edit(postId, request);

    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }


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

    //    @GetMapping("/posts")
//    public List<PostResponse> getList(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
//        return postService.getList(pageable);
//    }

}