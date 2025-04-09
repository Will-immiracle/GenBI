package com.will.bi.exception.utils;

import com.will.bi.common.ResultCodeEnum;
import com.will.bi.exception.BusinessException;

/**
 * @program: UserCenter
 * @description: 异常抛出工具类
 * @author: Mr.Zhang
 * @create: 2025-03-26 22:25
 **/
public class ThrowUtils {

    /**
     * 如果条件成立，则抛出异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    public static void throwIf(boolean condition, ResultCodeEnum resultCodeEnum) {
        throwIf(condition, BusinessException.build(resultCodeEnum));
    }

    public static void throwIf(boolean condition, ResultCodeEnum resultCodeEnum, String message) {
        throwIf(condition, BusinessException.build(resultCodeEnum, message));
    }
}
