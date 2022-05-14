package com.xie.crm.workbench.web.controller;

import com.xie.crm.commons.contants.Contants;
import com.xie.crm.commons.domain.ReturnObject;
import com.xie.crm.commons.utils.DateUtils;
import com.xie.crm.commons.utils.UUIDUtils;
import com.xie.crm.settings.domain.User;
import com.xie.crm.workbench.domain.ActivityRemark;
import com.xie.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping(value = "/workbench/activity/createActivityRemark")
    @ResponseBody
    public Object createActivityRemark(String noteContent, String activityId, HttpSession session){
        //封装参数
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDIT);
        activityRemark.setActivityId(activityId);
        //保存备注
        int result = 0;
        ReturnObject returnObject = new ReturnObject();
        try {
            result = activityRemarkService.saveCreateActivityRemark(activityRemark);
            //生成响应信息
            if(result > 0){
                //成功
                returnObject.setCode(Contants.CODE_SUCCESS);
            }else {
                //失败
                returnObject.setCode(Contants.CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后再试...");
            }
            returnObject.setRetData(activityRemark);
            return returnObject;
        } catch (Exception e) {
            e.printStackTrace();
            //生成响应信息
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试...");
            returnObject.setRetData(activityRemark);
            return returnObject;
        }
    }

    @RequestMapping(value = "/workbench/activity/deleteActivityRemark")
    @ResponseBody
    public Object deleteActivityRemark(String id){
        //执行删除业务
        int result = 0;
        ReturnObject returnObject = new ReturnObject();
        try {
            result = activityRemarkService.deleteActivityRemarkById(id);
            if(result > 0){
                //删除成功
                returnObject.setCode(Contants.CODE_SUCCESS);
            }else {
                //删除失败
                returnObject.setCode(Contants.CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //删除失败
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试...");
        }

        return returnObject;
    }

}
