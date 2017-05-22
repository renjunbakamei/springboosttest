/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/5/9.
 
 */
package com.jfpal.report.quartz;

import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.jfpal.report.util.AllocateEngine;
import com.jfpal.report.util.InformNoticeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("rejectTask")
public class AutoRejectTask {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(AutoRejectTask.class);

    @Autowired
    private ReportMapperImpl reportMapper;

    @Autowired
    private InformNoticeUtil informNoticeUtil;

    @Autowired
    private AllocateEngine allocateEngine;




}
