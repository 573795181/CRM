package com.myTest.crm.workbench.web.controller;

import com.myTest.crm.settings.domain.User;
import com.myTest.crm.settings.service.UserService;
import com.myTest.crm.settings.service.impl.UserServiceImpl;
import com.myTest.crm.utils.DateTimeUtil;
import com.myTest.crm.utils.PrintJson;
import com.myTest.crm.utils.ServiceFactory;
import com.myTest.crm.utils.UUIDUtil;
import com.myTest.crm.vo.PaginationVo;
import com.myTest.crm.workbench.domain.Activity;
import com.myTest.crm.workbench.domain.ActivityRemark;
import com.myTest.crm.workbench.service.ActivityService;
import com.myTest.crm.workbench.service.impl.ActivityServiceImpl;

import javax.security.auth.login.CredentialException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入市场活动控制器");
        String path = req.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if ("/workbench/activity/save.do".equals(path)){
            saveActivity(req,resp);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(req,resp);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(req,resp);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(req,resp);
        }else if("/workbench/activity/update.do".equals(path)){
            update(req,resp);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(req,resp);
        }else if ("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(req,resp);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(req,resp);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(req,resp);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(req,resp);
        }
    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行修改备注的操作");

        String id = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.updateRemark(ar);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(resp, map);
    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {
        String noteContent = req.getParameter("noteContent");
        String activityId =req.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark remark = new ActivityRemark();
        remark.setActivityId(activityId);
        remark.setNoteContent(noteContent);
        remark.setId(id);
        remark.setCreateBy(createBy);
        remark.setCreateTime(createTime);
        remark.setEditFlag(editFlag);

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.saveRemark(remark);

        Map<String,Object> map = new HashMap<String ,Object>();
        map.put("success",flag);
        map.put("i",remark);

        PrintJson.printJsonObj(resp,map);
    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到删除市场信息备注");

        String id = req.getParameter("id");
        System.out.println(id);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.deleteRemark(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到市场活动详细信息页备注");

        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarkList = activityService.getRemarkListByAid(id);

        PrintJson.printJsonObj(resp,remarkList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动详细信息页");
        String id = req.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.detail(id);
        req.setAttribute("a",activity);
//        System.out.println(activity);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);


    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动修改操作");

        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setStartDate(startDate);
        a.setOwner(owner);
        a.setName(name);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setEditBy(editBy);
        a.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(a);

        PrintJson.printJsonFlag(resp, flag);

    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");

        String id = req.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(resp, map);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动的删除操作");
        String ids[] = req.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(resp, flag);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进行查询市场活动信息列表操作（结合条件查询+分页查询）");
        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String statDate = req.getParameter("statDate");
        String endDate = req.getParameter("endDate");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("statDate",statDate);
        map.put("endDate",endDate);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVo<Activity> vo = activityService.pageList(map);
        PrintJson.printJsonObj(resp,vo);

    }

    private void saveActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.print("执行市场添加操作");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) req.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);


        boolean flag = as.save(activity);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(resp,userList);
    }
}
