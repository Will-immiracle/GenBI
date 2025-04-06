package com.will.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.will.model.dto.user.*;
import com.will.constant.UserConstant;
import com.will.exception.BusinessException;
import com.will.exception.utils.ThrowUtils;
import com.will.model.pojo.User;
import com.will.model.vo.user.UserLoginVO;
import com.will.model.vo.user.UserVO;
import com.will.service.UserService;
import com.will.common.Result;
import com.will.common.ResultCodeEnum;
import com.will.annotation.AuthCheck;
import com.will.utils.MD5Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: GenBI
 * @description: UserController类
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:43
 **/

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /*
    * controller层作用：
    *
    * 1. 请求路由与协议转化（url地址映射到实际业务）
    * 2. 输入参数校验（保证controller层输出参数合法性）
    * 3. 异常统一处理（RestControllerAdvice）
    * 4. 业务逻辑协调（只负责controller层，对接UI层与service层）
    * */
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        if(userRegisterDTO == null) throw BusinessException.build(ResultCodeEnum.PARAMS_ERROR);
        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        if(StringUtils.isEmpty(userAccount)) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        if(StringUtils.isEmpty(userPassword)) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR);
        if(StringUtils.isEmpty(checkPassword)) throw BusinessException.build(ResultCodeEnum.CHECK_PASSWORD_ERROR);
        Long userId = userService.userRegister(userAccount, userPassword, checkPassword);
        return Result.ok(userId);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if(userLoginDTO == null) throw BusinessException.build(ResultCodeEnum.PARAMS_ERROR);
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();
        if(StringUtils.isEmpty(userAccount)) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        if(StringUtils.isEmpty(userPassword)) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR);
        UserLoginVO user = userService.userLogin(userAccount, userPassword, request);
        return Result.ok(user);
    }
    /**
     * 用户注销
     *
     * 1. 优化检验
     */
    @PostMapping("/logout")
    public Result<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw BusinessException.build(ResultCodeEnum.PARAMS_ERROR,"未登录");
        }
        boolean result = userService.userLogout(request);
        return Result.ok(result);
    }

    /**
     * 获取当前登录用户
     *
     * 1. 获取用户登录信息
     * 2. 信息脱敏（封装VO），返回
     */
    @GetMapping("/get/login")
    public Result<UserLoginVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return Result.ok(userService.getUserLoginVO(user));
    }

    /**
     * 创建用户
     *
     * 1. 检验是否管理员
     * 2. 参数检验
     *
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw BusinessException.build(ResultCodeEnum.PARAMS_ERROR,"添加用户为空");
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String defaultPassword = "12345678";
        String encryptPassword = MD5Util.encrypt(defaultPassword);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ResultCodeEnum.OPERATION_ERROR);
        return Result.ok(user.getId());
    }

    /**
     * 删除用户
     *
     * 1. 检验参数
     * 2. 优化查询
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        if (userDeleteRequest == null || userDeleteRequest.getId() <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        boolean b = userService.removeById(userDeleteRequest.getId());
        return Result.ok(b);
    }

    /**
     * 更新用户
     *
     * 1. 检验参数
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ResultCodeEnum.OPERATION_ERROR);
        return Result.ok(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * 1. 检验参数
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ResultCodeEnum.PARAMS_ERROR, "用户不存在");
        return Result.ok(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * 1. 检验参数
     * 2. 信息脱敏（封装VO），返回
     */
    @GetMapping("/get/vo")
    public Result<UserVO> getUserVOById(long id, HttpServletRequest request) {
        Result<User> response = getUserById(id, request);
        User user = response.getData();
        return Result.ok(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * 1. 检验参数
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return Result.ok(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * 1. 检验参数
     * 2. 限制爬虫
     * 3. 数据脱敏与分页
     */
    @PostMapping("/list/page/vo")
    public Result<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                       HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCodeEnum.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return Result.ok(userVOPage);
    }

    /**
     * 更新个人信息
     *
     * 1. 检验参数
     */
    @PostMapping("/update/my")
    public Result<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ResultCodeEnum.OPERATION_ERROR);
        return Result.ok(true);
    }
}
