package com.will.common;

/**
 * @program: UserCenter
 * @description: 全局统一返回结果类
 * @author: Mr.Zhang
 * @create: 2025-03-26 22:25
 **/

public class Result<T> {
    //返回码
    private Integer code;
    //返回消息
    private String message;
    //返回数据
    private T data;
    //构建一个有数据的结果
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        if (data != null)
            result.setData(data);
        return result;
    }
    public static <T> Result<T> build(T data, Integer code, String message) {
        Result<T> result = build(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
    /*
     * 操作成功
     *
     * */
    public static <T> Result<T> ok(T data) {
        return Result.build(data, ResultCodeEnum.SUCCESS);
    }
    public Result<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }
    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
