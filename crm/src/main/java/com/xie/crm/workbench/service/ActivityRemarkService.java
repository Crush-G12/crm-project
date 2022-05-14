package com.xie.crm.workbench.service;

import com.xie.crm.workbench.domain.Activity;
import com.xie.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    List<Activity> queryActivityRemarkForDetailByActivityId(String activityId);

    int saveCreateActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

}
