package com.dzr.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * @author dingzr
 * @Description
 * @ClassName DataSourceConfig
 * @since 2017/7/5 13:43
 */

@Configuration
@MapperScan(basePackages = "com.dzr.mapper.primary", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class DataSourcePrimaryConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Autowired
//    public void setDataSource(@Qualifier("primaryDataSource") DataSource jb) {
//        setDataSource(jb);
//    }

    /**
     * jdbc的数据源加载
     * @throws Exception
     */
//    @Bean(name = "primaryJdbcTemplate")
//    @Primary
//    public JdbcTemplate primaryJdbcTemplate(
//            @Qualifier("primaryDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

//    @Value("${spring.datasource.primary.url}")
//    private String url;
//    @Value("${spring.datasource.primary.username}")
//    private String user;
//    @Value("${spring.datasource.primary.password}")
//    private String password;
//    @Value("${spring.datasource.primary.driverClassName}")
//    private String driverClass;
//    //主库数据源
//    @Bean(name = "primaryDataSource")
//    public DataSource PrimaryDataSource(){
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(url);
//        datasource.setUsername(user);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClass);
//        return datasource;
//    }


    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(primaryDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/primary/*.xml"));
        bean.setTypeAliasesPackage("com.dzr.po");
        return bean.getObject();
    }

    @Bean(name = "primaryTransactionManager")
    @Primary
    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
        return new DataSourceTransactionManager(primaryDataSource);
    }

    @Bean(name = "primarySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}