package com.myTest.crm.settings.service;

import com.myTest.crm.exception.LoginException;
import com.myTest.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String name, String pwd, String ip) throws LoginException;

    List<User> getUserList();
}
