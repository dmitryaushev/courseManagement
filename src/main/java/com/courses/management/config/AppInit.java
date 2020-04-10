package com.courses.management.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppInit implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Init Hibernate");
        HibernateDatabaseConnector.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Destroy Hibernate");
        HibernateDatabaseConnector.destroy();
    }
}
