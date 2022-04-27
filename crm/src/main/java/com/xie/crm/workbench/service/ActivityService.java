package com.xie.crm.workbench.service;

import com.xie.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveActivityService(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountByConditionForPage(Map<String,Object> map);
}


