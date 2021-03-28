package com.myTest.crm.settings.service.impl;

import com.myTest.crm.settings.dao.DicTypeDao;
import com.myTest.crm.settings.dao.DicValueDao;
import com.myTest.crm.settings.domain.DicType;
import com.myTest.crm.settings.domain.DicValue;
import com.myTest.crm.settings.service.DicService;
import com.myTest.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        List<DicType> dicTypeList = dicTypeDao.getTypeList();

        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();

        for (DicType dicType : dicTypeList){
            String code = dicType.getCode();

            List<DicValue> valueList = dicValueDao.getListByCode(code);

            map.put(code+"List",valueList);
        }

        return map;
    }
}
