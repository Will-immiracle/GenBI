package com.will.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.mapper.UserMapper;
import com.will.pojo.User;
import com.will.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author zhangzan
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-04-04 22:13:24
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




