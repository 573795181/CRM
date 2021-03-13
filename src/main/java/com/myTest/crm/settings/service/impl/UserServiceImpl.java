package com.myTest.crm.settings.service.impl;

import com.myTest.crm.exception.LoginException;
import com.myTest.crm.settings.dao.UserDao;
import com.myTest.crm.settings.domain.User;
import com.myTest.crm.settings.service.UserService;
import com.myTest.crm.utils.DateTimeUtil;
import com.myTest.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String name, String pwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String,String>();
        map.put("name",name);
        map.put("pwd",pwd);

        System.out.println();
        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("账号密码错误");
        }

        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账户已失效");
        }

        if ("0".equals(user.getLockState())){
            throw new LoginException("账户被锁定");
        }

        String userIp = user.getAllowIps();
        if (!userIp.contains(ip)){
            throw new LoginException("ip地址无效");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }
}
