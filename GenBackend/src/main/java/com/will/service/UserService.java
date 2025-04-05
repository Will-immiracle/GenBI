package com.will.service;

import com.will.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.will.utils.Result;

/**
* @author zhangzan
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-04-04 22:13:24
*/
public interface UserService extends IService<User> {
    /*
    * 注册用户
    *
    * @param userAccount 用户账号
    * @param userPassword 用户密码
    * @param checkPassword 校验密码
    * */

    Long register(String userAccount, String userPassword, String checkPassword);

    /*
    * 用户登录
    *
    * @param userAccount 用户账号
    * @param userPassword 用户密码
    * */
    Result login(String userAccount, String userPassword);
}
