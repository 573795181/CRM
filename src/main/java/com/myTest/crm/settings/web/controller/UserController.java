package com.myTest.crm.settings.web.controller;


import com.myTest.crm.settings.domain.User;
import com.myTest.crm.settings.service.UserService;
import com.myTest.crm.settings.service.impl.UserServiceImpl;
import com.myTest.crm.utils.MD5Util;
import com.myTest.crm.utils.PrintJson;
import com.myTest.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(req,resp);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("loginName");
        String password = request.getParameter("loginPwd");

        String pwd = MD5Util.getMD5(password);
        String ip = request.getRemoteAddr();

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user =  us.login(name,pwd,ip);
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }


}
