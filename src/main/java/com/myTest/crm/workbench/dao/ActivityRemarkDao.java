package com.myTest.crm.workbench.dao;


import com.myTest.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    int deleteByAids(String[] ids);

    int getCountByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String id);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark remark);

    int updateRemark(ActivityRemark ar);
}
