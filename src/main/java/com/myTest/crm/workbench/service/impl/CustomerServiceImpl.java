package com.myTest.crm.workbench.service.impl;

import com.myTest.crm.utils.SqlSessionUtil;
import com.myTest.crm.workbench.dao.CustomerDao;
import com.myTest.crm.workbench.domain.Customer;
import com.myTest.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);

        return sList;
    }
}
