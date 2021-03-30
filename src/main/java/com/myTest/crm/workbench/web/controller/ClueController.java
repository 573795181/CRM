package com.myTest.crm.workbench.web.controller;

import com.myTest.crm.settings.domain.User;
import com.myTest.crm.settings.service.UserService;
import com.myTest.crm.settings.service.impl.UserServiceImpl;
import com.myTest.crm.utils.DateTimeUtil;
import com.myTest.crm.utils.PrintJson;
import com.myTest.crm.utils.ServiceFactory;
import com.myTest.crm.utils.UUIDUtil;
import com.myTest.crm.workbench.domain.Activity;
import com.myTest.crm.workbench.domain.Clue;
import com.myTest.crm.workbench.domain.Tran;
import com.myTest.crm.workbench.service.ActivityService;
import com.myTest.crm.workbench.service.ClueService;
import com.myTest.crm.workbench.service.impl.ActivityServiceImpl;
import com.myTest.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到线索控制器");

        String path = req.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(req,resp);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(req,resp);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(req,resp);
        }else if ("/workbench/clue/unbund.do".equals(path)){
            unbund(req,resp);
        }else if ("/workbench/clue/getActivityListByNameNotRelation.do".equals(path)){
            getActivityListByNameNotRelation(req,resp);
        }else if ("/workbench/clue/bund.do".equals(path)){
            bund(req,resp);
        }else if ("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(req,resp);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(req,resp);
        }
    }

    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("执行线索转换的操作");

        String clueId = req.getParameter("clueId");
        String flag = req.getParameter("flag");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        Tran t = null;

        if ("a".equals(flag)){

            t = new Tran();

            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setId(id);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag1 = cs.convert(clueId,createBy,t);

        if (flag1){
            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
        }


    }

    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("查询市场活动列表（根据名称模糊查）");

        String aname = req.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(resp,aList);

    }

    private void bund(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行关联市场活动操作");

        String clueId = req.getParameter("clueId");
        String activityIds[] = req.getParameterValues("activityId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(clueId,activityIds);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getActivityListByNameNotRelation(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("查询市场活动列表（根据名称模糊查+排除掉已经关联指定线索的列表");

        String aname = req.getParameter("aname");
        String clueId = req.getParameter("clueId");

        Map<String,String> map = new HashMap<String,String>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByNameNotRelation(map);

        PrintJson.printJsonObj(resp,aList);

    }

    private void unbund(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("解除关联关系操作");

        String id = req.getParameter("id");

        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = service.unbund(id);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getActivityListByClueId(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据市场id查询关联的市场活动列表");

        String clueId = req.getParameter("clueId");

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activityList = service.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(resp,activityList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到线索详细信息页");

        String id = req.getParameter("id");

        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = service.detail(id);

        req.setAttribute("clue",clue);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);


        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = service.save(clue);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");

        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = service.getUserList();

        PrintJson.printJsonObj(resp,userList);
    }
}
