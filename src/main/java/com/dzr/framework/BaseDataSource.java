package com.dzr.framework;

import com.dzr.config.DataSourcePrimaryConfig;
import com.dzr.config.DataSourceSecondaryConfig;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author dingzr
 * @Description
 * @ClassName BaseDataSource
 * @since 2017/8/3 11:28
 */
public class BaseDataSource extends HttpServlet {

    @Autowired
    DataSourcePrimaryConfig dataSourcePrimaryConfig;
    @Autowired
    DataSourceSecondaryConfig dataSourceSecondaryConfig;

    @Override
    public void destroy() {
        super.destroy();
        try {
            Connection conn = dataSourcePrimaryConfig.primaryDataSource().getConnection();
            conn.close();
            Connection connS = dataSourceSecondaryConfig.secondaryDataSource().getConnection();
            connS.close();
            DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("destroy方法被执行");
    }

}
