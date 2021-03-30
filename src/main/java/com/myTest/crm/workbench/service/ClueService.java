package com.myTest.crm.workbench.service;

import com.myTest.crm.workbench.domain.Clue;
import com.myTest.crm.workbench.domain.Tran;

public interface ClueService {
    Boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] activityIds);

    boolean convert(String clueId, String createBy, Tran t);
}
