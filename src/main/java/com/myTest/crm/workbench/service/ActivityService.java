package com.myTest.crm.workbench.service;

import com.myTest.crm.workbench.domain.Activity;

import javax.servlet.http.HttpServletRequest;

public interface ActivityService {
    boolean save(Activity activity);
}
