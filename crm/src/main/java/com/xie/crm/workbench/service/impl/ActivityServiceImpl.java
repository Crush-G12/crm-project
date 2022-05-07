package com.xie.crm.workbench.service.impl;

import com.xie.crm.workbench.domain.Activity;
import com.xie.crm.workbench.mapper.ActivityMapper;
import com.xie.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveActivityService(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        List<Activity> activities = activityMapper.selectActivityByConditionForPage(map);
        return activities;
    }

    @Override
    public int queryCountByConditionForPage(Map<String, Object> map) {
        int count = activityMapper.selectCountOfActivityByCondition(map);
        return count;
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        int result = activityMapper.deleteActivityByIds(ids);
        return result;
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }
}
