package com.will.model.dto.user;

import lombok.Data;

/**
 * @program: GenBI
 * @description: UserController和UI层交互在Register上的接口类
 * @author: Mr.Zhang
 * @create: 2025-04-05 00:26
 **/

@Data
public class UserRegisterDTO {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
