package com.will.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.exception.BusinessException;
import com.will.mapper.UserMapper;
import com.will.pojo.User;
import com.will.service.UserService;
import com.will.utils.MD5Util;
import com.will.utils.Result;
import com.will.utils.ResultCodeEnum;
import org.springframework.stereotype.Service;

/**
* @author zhangzan
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-04-04 22:13:24
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /*
    * 注册用户
    *
    * 1. 检查输入参数、检查账号是否存在、密码是否合法、两次密码是否一致
    * 2. 密码加密处理
    * 3. 存入信息
    * */
    @Override
    public Long register(String userAccount, String userPassword, String checkPassword) {
        // 1.检验输入参数
        if(StringUtils.isEmpty(userAccount)) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        if(StringUtils.isEmpty(userPassword)) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR);
        if(StringUtils.isEmpty(checkPassword)) throw BusinessException.build(ResultCodeEnum.CHECK_PASSWORD_ERROR);
        if(!userPassword.equals(checkPassword)) throw BusinessException.build(ResultCodeEnum.CHECK_PASSWORD_ERROR,"两次输入密码不一致");
        if(userPassword.length() < 8 || userPassword.length() > 16) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR,"密码长度为8-16位");
        if(userAccount.length() <4 ) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR,"账号长度过短");
        if(userAccount.length() >16 ) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR,"账号长度过长");
        // 2.连接数据库，检查用户账号(加锁)，加密处理数据
        synchronized (userAccount.intern()){
            LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(userAccount,userAccount);
            if(this.baseMapper.selectCount(queryWrapper)>0) throw BusinessException.build(ResultCodeEnum.USERNAME_USED);
            User user = new User();
            user.setUserAccount(userAccount);
            String encrypt = MD5Util.encrypt(userPassword);
            user.setUserPassword(encrypt);
            int insert = this.baseMapper.insert(user);
            if(insert <= 0) throw BusinessException.build(ResultCodeEnum.DATABASE_ERROR);
            return user.getId();
        }
    }

    @Override
    public Result login(String userAccount, String userPassword) {
        return null;
    }
}




