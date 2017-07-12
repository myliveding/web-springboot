package com.dzr.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

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
        //下面这个是使用的Hikari连接池，相应的会出现一个属性叫jdbcurl
        return DataSourceBuilder.create().build();
    }


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