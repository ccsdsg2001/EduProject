package com.example.common.exception;

public enum CodeEnume {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION (10001,"参数格式校验失败");



    private  int code;
    private String msg;

    CodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
