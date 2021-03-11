package com.myTest.crm.settings.dao;

import com.myTest.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {

    User login(Map<String, String> map);
}
