package com.myTest.crm.workbench.dao;

import com.myTest.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {


    Customer getCustomerByName(String company);

    int save(Customer customer);
}
