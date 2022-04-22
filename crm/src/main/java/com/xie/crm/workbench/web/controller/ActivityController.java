package com.xie.crm.workbench.web.controller;

import com.xie.crm.settings.domain.User;
import com.xie.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/workbench/activity/index")
    public String index(HttpServletRequest request){
        //查询所有用户
        List<User> userList = userService.queryAllUsers();
        //保存到作用域中
        request.setAttribute("userList",userList);
        return "workbench/activity/index";
    }
}
