package com.will.exception;

import com.will.common.ResultCodeEnum;

/**
 * @program: GenBI
 * @description: 业务异常类
 * @author: Mr.Zhang
 * @create: 2025-04-04 23:53
 **/

public class BusinessException extends RuntimeException {

    /*
    * 状态码
    * */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    public static BusinessException build(ResultCodeEnum resultCodeEnum) {
        return new BusinessException(resultCodeEnum);
    }
    public static BusinessException build(ResultCodeEnum resultCodeEnum, String message) {
        return new BusinessException(resultCodeEnum.getCode(), message);
    }
}
