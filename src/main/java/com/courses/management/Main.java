package com.courses.management;

import com.courses.management.common.Console;
import com.courses.management.common.MainController;
import com.courses.management.common.View;
import com.courses.management.config.HibernateDatabaseConnector;

public class Main {

    public static void main(String[] args) {

        View view = new Console();
        HibernateDatabaseConnector.init();
        MainController controller = new MainController(view,
                HibernateDatabaseConnector.getSessionFactory());
        controller.read();
    }
}
