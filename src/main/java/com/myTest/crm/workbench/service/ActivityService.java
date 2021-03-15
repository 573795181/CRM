package com.myTest.crm.workbench.service;

import com.myTest.crm.vo.PaginationVo;
import com.myTest.crm.workbench.domain.Activity;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);
}
