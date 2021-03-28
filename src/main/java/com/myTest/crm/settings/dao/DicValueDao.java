package com.myTest.crm.settings.dao;

import com.myTest.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
