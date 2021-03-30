package com.myTest.crm.workbench.service;

import com.myTest.crm.vo.PaginationVo;
import com.myTest.crm.workbench.domain.Activity;
import com.myTest.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String id);

    boolean deleteRemark(String id);


    boolean saveRemark(ActivityRemark remark);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameNotRelation(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
