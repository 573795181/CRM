package com.myTest.crm.workbench.service.impl;

import com.myTest.crm.settings.dao.UserDao;
import com.myTest.crm.utils.SqlSessionUtil;
import com.myTest.crm.vo.PaginationVo;
import com.myTest.crm.workbench.dao.ActivityDao;
import com.myTest.crm.workbench.domain.Activity;
import com.myTest.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

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

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        int total =  activityDao.getTotal(map);
        List<Activity> activityList  = activityDao.getPageList(map);
        PaginationVo<Activity> vo = new PaginationVo<Activity>();
        vo.setTotal(total);
        vo.setDataList(activityList);
        return vo;
    }
}
