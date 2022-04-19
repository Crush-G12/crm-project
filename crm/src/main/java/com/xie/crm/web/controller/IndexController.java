package com.xie.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    //控制器的方法是public的，因为控制器要调用
    //给Controller方法分配URL:当访问“/”时，就会调用该Controller方法
    @RequestMapping(value = "/")
    public String index(){
        //请求转发，直接return页面的资源路径,视图解析器会自动加上前后缀
        return "index";
    }
}
