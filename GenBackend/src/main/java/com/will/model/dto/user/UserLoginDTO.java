package com.will.model.dto.user;

import lombok.Data;

/**
 * @program: GenBI
 * @description: UserLoginDTO
 * @author: Mr.Zhang
 * @create: 2025-04-05 10:51
 **/

@Data
public class UserLoginDTO {
    private String userAccount;
    private String userPassword;
}
