package com.zz.bi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zz.bi.model.dto.user.UserQueryRequest;
import com.zz.bi.model.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zz.bi.model.vo.user.UserLoginVO;
import com.zz.bi.model.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @program: GenBI
 * @description: 针对 表 user(用户) 的数据库操作
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:43
 **/

public interface UserService extends IService<User> {
    /**
    * 注册用户
    *
    * @param userAccount 用户账号
    * @param userPassword 用户密码
    * @param checkPassword 校验密码
    */

    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
    * 用户登录
    *
    * @param userAccount 用户账号
    * @param userPassword 用户密码
    */
    UserLoginVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否管理员
     *
     * @param request 请求
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否管理员
     *
     * @param user 用户
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request 请求
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 信息脱敏
     *
     * 注：VO指和业务有关的数据传输对象
     */
    UserLoginVO  getUserLoginVO(User user);

    UserVO getUserVO(User user);

    List<UserVO> getUserVO(List<User> userList);
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
