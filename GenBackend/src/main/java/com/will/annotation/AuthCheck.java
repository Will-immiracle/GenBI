package com.will.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: UserCenter
 * @description: 权限校验
 * @author: Mr.Zhang
 * @create: 2025-03-26 22:25
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某个角色
     *
     */
    String mustRole() default "";

}

