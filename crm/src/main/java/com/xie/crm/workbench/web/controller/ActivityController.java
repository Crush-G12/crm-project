package com.xie.crm.workbench.web.controller;

import com.xie.crm.commons.contants.Contants;
import com.xie.crm.commons.domain.ReturnObject;
import com.xie.crm.commons.utils.DateUtils;
import com.xie.crm.commons.utils.UUIDUtils;
import com.xie.crm.settings.domain.User;
import com.xie.crm.settings.service.UserService;
import com.xie.crm.workbench.domain.Activity;
import com.xie.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ActivityController {
    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    @RequestMapping(value = "/workbench/activity/index")
    public String index(HttpServletRequest request){
        //查询所有用户
        List<User> userList = userService.queryAllUsers();
        //保存到作用域中
        request.setAttribute("userList",userList);
        return "workbench/activity/index";
    }

    @RequestMapping(value = "/workbench/activity/createActivity")
    @ResponseBody
    public Object createActivity(Activity activity, HttpSession session){
        //用UUID生成id
        activity.setId(UUIDUtils.getUUID());
        //生成创建时间
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        //获取作者的id
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());
        //保存数据
        ReturnObject returnObject = new ReturnObject();
        try {
            int result = activityService.saveActivityService(activity);
            //如果成功
            if(result > 0){
                returnObject.setCode(Contants.CODE_SUCCESS);
            }else {
                //如果失败
                returnObject.setCode(Contants.CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后再试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //如果失败
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试....");
        }
        return returnObject;
    }

}
