package com.myTest.crm.workbench.dao;

import com.myTest.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    int getTotal(Map<String, Object> map);

    List<Activity> getPageList(Map<String, Object> map);
}
