package com.myTest.crm.settings.service.impl;

import com.myTest.crm.settings.dao.UserDao;
import com.myTest.crm.settings.service.UserService;
import com.myTest.crm.utils.SqlSessionUtil;


public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
}
