package com.xie.crm.settings.web.controller;

import com.xie.crm.commons.contants.Contants;
import com.xie.crm.commons.domain.ReturnObject;
import com.xie.crm.commons.utils.DateUtils;
import com.xie.crm.settings.domain.User;
import com.xie.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    //注入Service层的对象
    @Autowired
    UserService userService;

    /*
    1.当Controller处理完请求后，响应信息返回的页面是login.jsp，
    那么url就要和login.jsp的资源目录保持一致，资源名和方法名保持一致
    2.Controller在处理请求时，会自动加上前缀，这里的目录会加上视图解析器的根“/WEB-INF/pages，所以前面写一个斜杠/”
     */
    @RequestMapping(value = "/settings/qx/user/toLogin")
    public String toLogin(){
        //视图解析器有了/，这里的url不用加/
        return "settings/qx/user/login";
    }

    @RequestMapping(value = "/settings/qx/user/login")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRem, HttpServletRequest request, HttpSession httpSession){
        //@ResponseBody:将要返回的数据封装成java对象后，@ResponseBody会
        // 将java对象转为json格式的数据,虽然返回值是Object，但返回时会返回实际的数据类型json（多态性）
        //将前端传来的参数封装好
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //将封装好的的对象传给Service层
        User user = userService.queryUserByLoginActAndPwd(map);
        //获得查询结果后，进一步判断，最后生成响应信息
        ReturnObject returnObject = new ReturnObject();
        if(user == null){
            //用户名或密码有误
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else {
            //账号和密码正确，判断账号是否过期
            String expireTime = user.getExpireTime();
            String format = DateUtils.formatDateTime(new Date());
            if(expireTime.compareTo(format) < 0){
                //小于0说明前者比后者小,说明账号已经过期
                returnObject.setCode(Contants.CODE_FAIL);
                returnObject.setMessage("账号已经过期");
            }else if(user.getLockState().equals("0")){
                //若是0，说明账号被锁定
                returnObject.setCode(Contants.CODE_FAIL);
                returnObject.setMessage("账号被锁定");
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                //如果不是用户的常用地址，也不允许登录
                returnObject.setCode(Contants.CODE_FAIL);
                returnObject.setMessage("ip地址受限制");
            }else{
                //登录成功
                returnObject.setCode(Contants.CODE_SUCCESS);
                //保存到Session域
                httpSession.setAttribute(Contants.SESSION_USER,user);
            }
        }
        //@ResponseBody会将java对象转为json格式的数据，返回给浏览器
        return returnObject;
    }

}
