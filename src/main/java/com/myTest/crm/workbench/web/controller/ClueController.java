package com.myTest.crm.workbench.web.controller;

import com.myTest.crm.settings.domain.User;
import com.myTest.crm.settings.service.UserService;
import com.myTest.crm.settings.service.impl.UserServiceImpl;
import com.myTest.crm.utils.DateTimeUtil;
import com.myTest.crm.utils.PrintJson;
import com.myTest.crm.utils.ServiceFactory;
import com.myTest.crm.utils.UUIDUtil;
import com.myTest.crm.workbench.domain.Clue;
import com.myTest.crm.workbench.service.ClueService;
import com.myTest.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到线索控制器");

        String path = req.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(req,resp);
        }
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
