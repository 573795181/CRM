package com.myTest.crm.workbench.dao;

import com.myTest.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {


    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);
}
