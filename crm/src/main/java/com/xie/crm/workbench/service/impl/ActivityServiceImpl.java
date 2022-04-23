package com.xie.crm.workbench.service.impl;

import com.xie.crm.workbench.domain.Activity;
import com.xie.crm.workbench.mapper.ActivityMapper;
import com.xie.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveActivityService(Activity activity) {
        return activityMapper.insertActivity(activity);
    }
}
