package com.myTest.crm.workbench.dao;


public interface ActivityRemarkDao {

    int deleteByAids(String[] ids);

    int getCountByAids(String[] ids);
}
