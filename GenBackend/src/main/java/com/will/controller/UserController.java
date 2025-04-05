package com.will.controller;

import com.alibaba.druid.util.StringUtils;
import com.will.exception.BusinessException;
import com.will.model.dto.user.UserLoginDTO;
import com.will.model.dto.user.UserRegisterDTO;
import com.will.model.vo.user.UserLoginVO;
import com.will.service.UserService;
import com.will.common.Result;
import com.will.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/register")
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

    @RequestMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if(userLoginDTO == null) throw BusinessException.build(ResultCodeEnum.PARAMS_ERROR);
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();
        if(StringUtils.isEmpty(userAccount)) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        if(StringUtils.isEmpty(userPassword)) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR);
        UserLoginVO user = userService.userLogin(userAccount, userPassword, request);
        return Result.ok(user);
    }


}
