/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/7.
 
 */
package com.jfpal.core.config.jdbc;


import com.alibaba.druid.pool.DruidDataSource;
import com.jfpal.core.config.prop.DruidProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "druid", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@MapperScan(basePackages = "com.jfpal.report.mapper")
public class DruidAutoConfiguration {
    @Autowired
    private DruidProperties properties;

    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource=new DruidDataSource();
        try {
            dataSource.setUrl(properties.getUrl());
            dataSource.setUsername(properties.getUsername());
            dataSource.setPassword(properties.getPassword());
            if(properties.getInitialSize()>0){
                dataSource.setInitialSize(properties.getInitialSize());
            }
            if(properties.getMinIdle()>0){
                dataSource.setMinIdle(properties.getMinIdle());
            }
            if(properties.getMaxActive()>0){
                dataSource.setMaxActive(properties.getMaxActive());
            }
            dataSource.setTestOnBorrow(properties.isTestOnBorrow());
            dataSource.setFilters(properties.getFilters());
            dataSource.setMaxWait(properties.getMaxWait());
            dataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
            dataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
            dataSource.setValidationQuery(properties.getValidationQuery());
            dataSource.setTestWhileIdle(properties.isTestWhileIdle());
            dataSource.setTestOnReturn(properties.isTestOnReturn());
            dataSource.setPoolPreparedStatements(properties.isPoolPreparedSStements());
            dataSource.setMaxOpenPreparedStatements(properties.getMaxOpenPreparedStatements());
            dataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return dataSource;
    }
}
