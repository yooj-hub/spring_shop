package com.yoojshop.api.exception;

import lombok.Getter;

public class InvalidRequest extends YoojShopException{


    private static final String MESSAGE = "존재하지 않는 요청입니다.";
    public InvalidRequest() {
        super(MESSAGE);
    }

    private  String fieldName;
    private  String message;

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    public InvalidRequest(String fieldName, String message){
        super(MESSAGE);
        addValidation(fieldName,message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
