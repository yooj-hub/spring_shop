package com.yoojshop.api.controller;

import com.yoojshop.api.exception.InvalidRequest;
import com.yoojshop.api.exception.YoojShopException;
import com.yoojshop.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public Map<String, String> invalidRequestHandler(MethodArgumentNotValidException e){
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError fieldError : e.getFieldErrors()) {
//            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        return errors;
//    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        Map<String, String> validation = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            validation.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .validation(validation)
                .build();

    }

    @ResponseBody
    @ExceptionHandler(YoojShopException.class)
    public ResponseEntity<ErrorResponse> yoojShopException(YoojShopException e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(Integer.toString(e.getStatusCode()))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        // 응답 json validation -> title : 포함되어야한다.


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(e.getStatusCode()).body(body);
        return response;


    }


}
