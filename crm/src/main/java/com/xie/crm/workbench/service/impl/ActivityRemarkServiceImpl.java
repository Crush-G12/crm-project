package com.xie.crm.workbench.service.impl;

import com.xie.crm.workbench.domain.Activity;
import com.xie.crm.workbench.domain.ActivityRemark;
import com.xie.crm.workbench.mapper.ActivityMapper;
import com.xie.crm.workbench.mapper.ActivityRemarkMapper;
import com.xie.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Override
    public List<Activity> queryActivityRemarkForDetailByActivityId(String activityId) {
        return activityRemarkMapper.selectActivityRemarkForDetailByActivityId(activityId);
    }

    @Override
    public int saveCreateActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertActivityRemark(activityRemark);
    }
}
