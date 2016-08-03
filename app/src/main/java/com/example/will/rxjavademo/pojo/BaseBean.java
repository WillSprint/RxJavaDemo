package com.example.will.rxjavademo.pojo;

public class BaseBean<T> {

    private String code;

    private String status;

    private String msg;

    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ReturnBean [code=" + code + ", message=" + msg
                + ", data=" + data + "]";
    }

}
