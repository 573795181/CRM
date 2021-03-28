package com.myTest.crm.workbench.service.impl;

import com.myTest.crm.utils.SqlSessionUtil;
import com.myTest.crm.workbench.dao.ActivityRemarkDao;
import com.myTest.crm.workbench.dao.ClueDao;
import com.myTest.crm.workbench.domain.Clue;
import com.myTest.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSessionFactory;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);

    @Override
    public Boolean save(Clue clue) {
        int result = clueDao.save(clue);

        Boolean flag = true;

        if (result!=1){
            flag = false;
        }

        return flag;
    }
}
