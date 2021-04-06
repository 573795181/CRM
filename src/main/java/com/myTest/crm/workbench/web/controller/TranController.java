package com.myTest.crm.workbench.web.controller;

import com.myTest.crm.settings.domain.User;
import com.myTest.crm.settings.service.UserService;
import com.myTest.crm.settings.service.impl.UserServiceImpl;
import com.myTest.crm.utils.DateTimeUtil;
import com.myTest.crm.utils.PrintJson;
import com.myTest.crm.utils.ServiceFactory;
import com.myTest.crm.utils.UUIDUtil;
import com.myTest.crm.workbench.domain.Customer;
import com.myTest.crm.workbench.domain.Tran;
import com.myTest.crm.workbench.domain.TranHistory;
import com.myTest.crm.workbench.service.CustomerService;
import com.myTest.crm.workbench.service.TranService;
import com.myTest.crm.workbench.service.impl.CustomerServiceImpl;
import com.myTest.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet{

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到交易控制器");

        String path = req.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
                add(req,resp);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
                getCustomerName(req,resp);
        } else if ("/workbench/transaction/save.do".equals(path)) {
            save(req,resp);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(req,resp);
        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)){
            getHistoryListByTranId(req,resp);
        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(req,resp);
        }
    }

    private void changeStage(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行改变阶段的操作");

        String id = req.getParameter("id");
        String stage = req.getParameter("stage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);

        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(resp, map);

    }

    private void getHistoryListByTranId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据交易id取得相应的历史列表");

        String tranId = req.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList = ts.getHistoryListByTranId(tranId);

        Map<String,String> pMap = (Map<String, String>) req.getServletContext().getAttribute("pMap");

        for (TranHistory th : thList){

            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }

        PrintJson.printJsonObj(resp,thList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id = req.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) req.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        req.setAttribute("t",t);
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("执行添加交易操作");

        String id= UUIDUtil.getUUID();
        String owner= req.getParameter("owner");
        String money= req.getParameter("money");
        String name= req.getParameter("name");
        String expectedDate= req.getParameter("expectedDate");
        String customerName= req.getParameter("customerName");
        String stage= req.getParameter("stage");
        String type= req.getParameter("type");
        String source= req.getParameter("source");
        String activityId= req.getParameter("activityId");
        String contactsId= req.getParameter("contactsId");
        String createBy= ((User)req.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        String description= req.getParameter("description");
        String contactSummary= req.getParameter("contactSummary");
        String nextContactTime= req.getParameter("nextContactTime");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(tran,customerName);

        if (flag){

            resp.sendRedirect(req.getContextPath() + "/workbench/transaction/index.jsp");

        }


    }

    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得 客户名称列表(按照客户名称进行模糊查询)");

        String name = req.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(resp,sList);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到跳转交易添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = us.getUserList();

        req.setAttribute("uList",userList);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);
    }
}
