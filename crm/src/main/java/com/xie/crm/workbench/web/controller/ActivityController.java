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
import java.util.*;

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

    @RequestMapping(value = "/workbench/activity/queryActivityByConditionForPage")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,
                                                  String endDate,Integer pageNo,Integer pageSize){
        //封装到Map里
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        Integer beginNo;
        if(pageNo != null && pageSize != null){
            beginNo = (pageNo-1)*pageSize;
        }else {
            beginNo = null;
        }
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);
        //查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalCount = activityService.queryCountByConditionForPage(map);
        //生成json字符串
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("activityList",activityList);
        resultMap.put("totalCount",totalCount);

        return resultMap;
    }

}
