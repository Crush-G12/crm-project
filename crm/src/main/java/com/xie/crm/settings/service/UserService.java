package com.xie.crm.settings.service;

import com.xie.crm.settings.domain.User;
import com.xie.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);

    List<User> queryAllUsers();

}
