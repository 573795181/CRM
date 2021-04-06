package com.myTest.crm.workbench.dao;

import com.myTest.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {


    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
