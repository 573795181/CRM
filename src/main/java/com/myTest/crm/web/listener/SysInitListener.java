package com.myTest.crm.web.listener;

import com.myTest.crm.settings.domain.DicValue;
import com.myTest.crm.settings.service.DicService;
import com.myTest.crm.settings.service.impl.DicServiceImpl;
import com.myTest.crm.utils.ServiceFactory;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("监听器对象已创建");

        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();

        DicService service = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = service.getAll();

        Set<String> set = map.keySet();
        for (String key:set){

            application.setAttribute(key,map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");
    }
}
