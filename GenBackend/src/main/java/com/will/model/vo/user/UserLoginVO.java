package com.will.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: GenBI
 * @description: UserLoginVO, 信息脱敏
 * @author: Mr.Zhang
 * @create: 2025-04-05 10:55
 **/

@Data
public class UserLoginVO implements Serializable {
    /**
     * token值
     */
    private String token;

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    private static final long serialVersionUID = 1L;
}
