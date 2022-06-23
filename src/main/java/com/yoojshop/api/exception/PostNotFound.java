package com.yoojshop.api.exception;

public class PostNotFound extends YoojShopException{


    private static final String message= "존재하지 않는 글입니다.";
    public PostNotFound() {
        super(message);
    }

    public PostNotFound(Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
