package com.xie.crm.settings.service.impl;

import com.xie.crm.settings.domain.User;
import com.xie.crm.settings.mapper.UserMapper;
import com.xie.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    //使用自动装配获得Mapper对象（service层调用dao层）
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }
}
