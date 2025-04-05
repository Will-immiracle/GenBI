package com.will.model.dto.user;

import java.io.Serializable;

import com.will.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: GenBI
 * @description: 用户查询请求
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:43
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}