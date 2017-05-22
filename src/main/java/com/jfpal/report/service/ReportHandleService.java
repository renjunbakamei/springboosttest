/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/11.
 
 */
package com.jfpal.report.service;

import com.jfpal.report.entity.Client;
import com.jfpal.report.entity.Report;
import com.jfpal.report.entity.retrunMsg.NoticeRetunModel;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.jfpal.report.util.AffairQueueUtil;
import com.jfpal.report.util.AllocateEngine;
import com.jfpal.report.util.InformNoticeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReportHandleService {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(ReportHandleService.class);

    @Autowired
    private AllocateEngine allocateEngine;

    @Autowired
    private AffairQueueUtil affairQueueUtil;

    @Autowired
    private ReportMapperImpl reportMapperImpl;

    @Autowired
    private InformNoticeUtil informNoticeUtil;

    @Async
    public void informReportOver(int id,String ip){
        Report report= reportMapperImpl.getReport(id);
        synchronized(AllocateEngine.lockObject) {
            try {
                List<Client> list = allocateEngine.getUsefulClients(0);
                Client client = allocateEngine.getClientByIp(list, ip);
                if (client == null) {
                    logger.error("没有此机器");
                } else {
                    //TODO 进行重发
                    String newJob = affairQueueUtil.getHeadAffair();
                    long allCount = client.getCount() - Long.parseLong(report.getReportCount());
                    client.setCount(allCount);
                    if (newJob == null) {
                        logger.info("任务队列中没有任务");
                        client.setSystomStatus(true);
                    } else {
                        if (allCount == 0) {
                            client.setSystomStatus(true);
                            Report newReport = reportMapperImpl.getReport(Integer.parseInt(newJob));
                            client = allocateEngine.allocate(newReport, client);
                        } else {
                            if (client.isSystomStatus()) {
                                Report newReport = reportMapperImpl.getReport(Integer.parseInt(newJob));
                                client = allocateEngine.allocate(newReport, client);
                            }
                        }
                    }
                    list.add(client);
                    Collections.sort(list);
                    allocateEngine.putClientList(list);
                }
                //通知前端消息提醒系统
                NoticeRetunModel model = informNoticeUtil.noticeReportAndResponse(report);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("执行通知pm");
    }

}
