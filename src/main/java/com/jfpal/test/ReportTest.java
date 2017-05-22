/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/5/9.
 
 */
package com.jfpal.test;


import com.jfpal.Application;
import com.jfpal.report.entity.Report;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ReportTest {
    @Autowired
    private ReportMapperImpl reportMapperImpl;

    @Test
    public void test(){
        Report report=new Report();
        report.setStatus("01");
        List<Report> list=reportMapperImpl.getReportList(report);
    }
}
