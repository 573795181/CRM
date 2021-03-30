package com.myTest.crm.workbench.service.impl;

import com.myTest.crm.settings.dao.UserDao;
import com.myTest.crm.settings.domain.User;
import com.myTest.crm.utils.SqlSessionUtil;
import com.myTest.crm.vo.PaginationVo;
import com.myTest.crm.workbench.dao.ActivityDao;
import com.myTest.crm.workbench.dao.ActivityRemarkDao;
import com.myTest.crm.workbench.domain.Activity;
import com.myTest.crm.workbench.domain.ActivityRemark;
import com.myTest.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao remarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);


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

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = remarkDao.getCountByAids(ids);
        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = remarkDao.deleteByAids(ids);
        if(count1!=count2){
            flag = false;
        }

        int count3 = activityDao.delete(ids);
        if(count3!=ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> uList = userDao.getUserList();

        //取a
        Activity a = activityDao.getById(id);

        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList", uList);
        map.put("a", a);

        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flag = true;
        int count = activityDao.update(a);
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String id) {
        List<ActivityRemark> remarkList = remarkDao.getRemarkListByAid(id);

        return remarkList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        System.out.println(id);
        int result = remarkDao.deleteRemark(id);
        if (result!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark remark) {
        int result = remarkDao.saveRemark(remark);
        boolean flag = true;
        if (result!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = remarkDao.updateRemark(ar);
        if(count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> activityList = activityDao.getActivityListByClueId(clueId);


        return activityList;
    }

    @Override
    public List<Activity> getActivityListByNameNotRelation(Map<String, String> map) {

        List<Activity> aList = activityDao.getActivityListByNameNotRelation(map);

        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {

        List<Activity> aList = activityDao.getActivityListByName(aname);

        return aList;
    }
}
