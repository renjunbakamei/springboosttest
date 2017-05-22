/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/1.
 
 */
package com.jfpal.report.quartz;

import com.jfpal.report.entity.Client;
import com.jfpal.report.entity.Report;
import com.jfpal.report.entity.retrunMsg.NoticeRetunModel;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.jfpal.report.util.AllocateEngine;
import com.jfpal.report.util.InformNoticeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 *数据重发通过
 */
@Component("quartzTask")
public class QuartzTask {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(QuartzTask.class);

    @Autowired
    private ReportMapperImpl reportMapper;

    @Autowired
    private InformNoticeUtil informNoticeUtil;

    @Autowired
    private AllocateEngine allocateEngine;

    @Value("${allocate.rowCount}")
    private Integer rowCount;

    public void run(){
        try {
            List<Report> reportList=reportMapper.getListByReport(rowCount);
            List<Client> list= null;
            Client client=null;
            for(Report report:reportList){
                NoticeRetunModel model=informNoticeUtil.noticeReportAndResponse(report);
                list=allocateEngine.getUsefulClients(0);
                client=allocateEngine.getClientByIp(list,report.getServerUrl());
                if(client==null){
                    continue;
                }
                long allCount=client.getCount()-Long.parseLong(report.getReportCount());
                if(allCount<=0){
                    client.setSystomStatus(true);
                    allCount=0;
                }
                client.setCount(allCount);
                list.add(client);
                Collections.sort(list);
                allocateEngine.putClientList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
