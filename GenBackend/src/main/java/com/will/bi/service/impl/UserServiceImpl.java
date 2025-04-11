package com.will.bi.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.bi.constant.CommonConstant;
import com.will.bi.exception.BusinessException;
import com.will.bi.mapper.UserMapper;
import com.will.bi.model.dto.user.UserQueryRequest;
import com.will.bi.model.enums.UserRoleEnum;
import com.will.bi.model.pojo.User;
import com.will.bi.model.vo.user.UserLoginVO;
import com.will.bi.model.vo.user.UserVO;
import com.will.bi.service.UserService;
import com.will.bi.utils.JwtHelper;
import com.will.bi.utils.MD5Util;
import com.will.bi.common.ResultCodeEnum;
import com.will.bi.utils.SqlUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.will.bi.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @program: GenBI
 * @description: 针对 表 user(用户) 的数据库操作
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:43
 **/

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
    * 注册用户
    *
    * 1. 检查输入参数、检查账号是否存在、密码是否合法、两次密码是否一致
    * 2. 密码加密处理
    * 3. 存入信息
    */
    @Override
    public Long userRegister(String userName, String userAccount, String userPassword, String checkPassword) {
        // 1.检验输入参数
        if(StringUtils.isEmpty(userAccount)) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        if(StringUtils.isEmpty(userPassword)) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR);
        if(StringUtils.isEmpty(checkPassword)) throw BusinessException.build(ResultCodeEnum.CHECK_PASSWORD_ERROR);
        if(!userPassword.equals(checkPassword)) throw BusinessException.build(ResultCodeEnum.CHECK_PASSWORD_ERROR,"两次输入密码不一致");
        if(userPassword.length() < 8 || userPassword.length() > 16) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR,"密码长度为8-16位");
        if(userAccount.length() <4 ) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR,"账号长度过短");
        if(userAccount.length() >16 ) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR,"账号长度过长");
        // 检验账号是否符合规范（只允许字母、数字、下划线）
        String validPattern = "^[a-zA-Z0-9_]+$";
        Pattern pattern=Pattern.compile(validPattern);
        Matcher matcher = pattern.matcher(userAccount);
        if (!matcher.matches()){
            throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR,"账号只允许字母、数字、下划线");
        }
        // 2.连接数据库，检查用户账号(加锁)，加密处理数据
        synchronized (userAccount.intern()){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(User::getUserAccount,userAccount);
            if(this.baseMapper.selectCount(queryWrapper)>0) throw BusinessException.build(ResultCodeEnum.USERNAME_USED);
            User user = new User();
            user.setUserName(userName);
            user.setUserAccount(userAccount);
            String encrypt = MD5Util.encrypt(userPassword);
            user.setUserPassword(encrypt);
            int insert = this.baseMapper.insert(user);
            if(insert <= 0) throw BusinessException.build(ResultCodeEnum.OPERATION_ERROR);
            return user.getId();
        }
    }

    /**
    * 用户登录
    *
    * 1. 检查输入参数、检查账号是否存在、密码是否正确
    * 2. 封装、返回脱敏数据、token值
    */
    @Override
    public UserLoginVO userLogin(String userAccount, String userPassword,HttpServletRequest request) {
        // 1.参数检验、账号密码格式检验（优化查询）
        if(StringUtils.isEmpty(userAccount)) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        if(StringUtils.isEmpty(userPassword)) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR);
        if(userPassword.length() < 8 || userPassword.length() > 16) throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR);
        if(userAccount.length() <4 ) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        if(userAccount.length() >16 ) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR);
        // 2.密码加密，校验用户信息
        String encrypt = MD5Util.encrypt(userPassword);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserAccount,userAccount);
        if(this.baseMapper.selectCount(queryWrapper) == 0) throw BusinessException.build(ResultCodeEnum.USERNAME_ERROR,"用户不存在");
        queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserAccount,userAccount);
        queryWrapper.eq(User::getUserPassword,encrypt);
        User user = this.baseMapper.selectOne(queryWrapper);
        if(user == null){
            log.error("user login failed, userAccount cannot match userPassword");
            throw BusinessException.build(ResultCodeEnum.PASSWORD_ERROR,"密码错误");
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        // 配置token信息
        JwtHelper jwtHelper = new JwtHelper();
        jwtHelper.setTokenExpiration(1000*60*60*24*7);
        jwtHelper.setTokenSignKey("GenBi");
        userLoginVO.setToken(jwtHelper.createToken(user.getId()));
        // 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return userLoginVO;
    }

    /**
     * 获取登录用户
     *
     * 1. 判断是否已登录
     * 2. 封装数据，返回
     */
    public User getLoginUser(HttpServletRequest request) {
        // 1.判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw BusinessException.build(ResultCodeEnum.NOTLOGIN);
        }
        // 2.从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser =this.getById(userId);
        if(currentUser == null){
            throw BusinessException.build(ResultCodeEnum.NOTLOGIN);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * 1. 判断是否登录
     * 2. 移除登录态
     */

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 1. 判断是否登录
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw BusinessException.build(ResultCodeEnum.OPERATION_ERROR, "未登录");
        }
        // 2. 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserLoginVO getUserLoginVO(User user) {
        if (user == null) {
            return null;
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        return userLoginVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw BusinessException.build(ResultCodeEnum.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(!StringUtils.isEmpty(userRole), "userRole", userRole);
        queryWrapper.like(!StringUtils.isEmpty(userProfile), "userProfile", userProfile);
        queryWrapper.like(!StringUtils.isEmpty(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }



}




