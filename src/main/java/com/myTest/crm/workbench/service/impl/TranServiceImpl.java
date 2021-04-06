package com.myTest.crm.workbench.service.impl;

import com.myTest.crm.utils.DateTimeUtil;
import com.myTest.crm.utils.SqlSessionUtil;
import com.myTest.crm.utils.UUIDUtil;
import com.myTest.crm.workbench.dao.CustomerDao;
import com.myTest.crm.workbench.dao.TranDao;
import com.myTest.crm.workbench.dao.TranHistoryDao;
import com.myTest.crm.workbench.domain.Customer;
import com.myTest.crm.workbench.domain.Tran;
import com.myTest.crm.workbench.domain.TranHistory;
import com.myTest.crm.workbench.service.TranService;

import java.util.List;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran tran, String customerName) {

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        if (customer==null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());
            customer.setContactSummary(tran.getContactSummary());

            int result1 = customerDao.save(customer);
            if (result1!=1){
                flag = false;
            }
        }

        tran.setCustomerId(customer.getId());

        int result2 = tranDao.save(tran);
        if (result2!=1){
            flag =false;
        }

        TranHistory tranHistory = new TranHistory();

        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(tran.getCreateTime());

        int result3 = tranHistoryDao.save(tranHistory);
        if (result3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;

        int count1 = tranDao.changeStage(t);
        if(count1!=1){

            flag = false;

        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());

        int count2 = tranHistoryDao.save(th);
        if(count2!=1){

            flag = false;

        }

        return flag;
    }
}
