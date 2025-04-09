package com.zz.bi.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * @program: UserCenter
 * @description: 删除请求
 * @author: Mr.Zhang
 * @create: 2025-03-26 22:25
 **/
@Data
public class UserDeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}