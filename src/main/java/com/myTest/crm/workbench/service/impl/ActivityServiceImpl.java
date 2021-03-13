package com.myTest.crm.workbench.service.impl;

import com.myTest.crm.settings.dao.UserDao;
import com.myTest.crm.utils.SqlSessionUtil;
import com.myTest.crm.workbench.dao.ActivityDao;
import com.myTest.crm.workbench.domain.Activity;
import com.myTest.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);


    @Override
    public boolean save(Activity activity) {
        boolean flag = true;
        int result = activityDao.save(activity);
        if (result!=1){
            flag = false;
        }
        return flag;
    }
}
