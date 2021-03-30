package com.myTest.crm.workbench.dao;

import com.myTest.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {


    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
