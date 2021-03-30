package com.myTest.crm.workbench.service.impl;

import com.myTest.crm.utils.DateTimeUtil;
import com.myTest.crm.utils.SqlSessionUtil;
import com.myTest.crm.utils.UUIDUtil;
import com.myTest.crm.workbench.dao.*;
import com.myTest.crm.workbench.domain.*;
import com.myTest.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public Boolean save(Clue clue) {
        int result = clueDao.save(clue);

        Boolean flag = true;

        if (result!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int result = clueActivityRelationDao.unbund(id);

        if (result!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bund(String clueId, String[] activityIds) {

        boolean flag = true;

        for (String activityId:activityIds){

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(activityId);
            car.setClueId(clueId);

            int result = clueActivityRelationDao.bund(car);
            if (result!=1){
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, String createBy, Tran t) {

        String createTime = DateTimeUtil.getSysTime();
        Boolean flag = true;

        Clue clue = clueDao.getById(clueId);


        String company = clue.getCompany();

        Customer customer = customerDao.getCustomerByName(company);


        if (customer==null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());

            int result = customerDao.save(customer);

            if (result!=1){
                flag = false;
            }
        }

        Contacts contacts = new Contacts();
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(clue.getId());
        contacts.setCreateTime(clue.getCreateTime());
        contacts.setCreateBy(clue.getCreateBy());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());

        int reslut2 = contactsDao.save(contacts);

        if (reslut2!=1){
            flag = false;
        }

        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark:clueRemarkList){

            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");
            int result3 = customerRemarkDao.save(customerRemark);

            if (result3!=1){
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setEditFlag("0");
            int result4 = contactsRemarkDao.save(contactsRemark);

            if (result4!=1){
                flag = false;
            }

        }

        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation: clueActivityRelationList){

            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());

            int result5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (result5!=1){
                flag = false;
            }
        }

        if (t!=null){

            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(contacts.getId());

            int result6 = tranDao.save(t);
            if (result6!=1){
                flag=false;
            }

            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(t.getStage());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setTranId(t.getId());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setCreateTime(t.getCreateTime());
            tranHistory.setCreateBy(t.getCreateBy());

            int result7 = tranHistoryDao.save(tranHistory);
            if (result7!=1){
                flag = false;
            }

        }

        for (ClueRemark clueRemark: clueRemarkList){

            int result8 = clueRemarkDao.delete(clueRemark);
            if (result8!=1){
                flag = false;
            }
        }

        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            int result9 = clueActivityRelationDao.delete(clueActivityRelation);
            if (result9!=1){
                flag = false;
            }
        }

        int result10 = clueDao.delete(clueId);
        if (result10!=1){
            flag = false;
        }

        return flag;
    }
}
