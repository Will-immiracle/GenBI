package com.will.bi.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * @program: GenBI
 * @description: 用户更新个人信息请求
 * @author: Mr.Zhang
 * @create: 2025-04-04 23:53
 **/

@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    private static final long serialVersionUID = 1L;
}