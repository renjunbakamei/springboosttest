/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/20.
 
 */
package com.jfpal.report.quartz;


import com.fasterxml.jackson.core.type.TypeReference;
import com.jfpal.constants.CoreConstants;
import com.jfpal.core.utils.JsonUtil;
import com.jfpal.core.utils.MapUtil;
import com.jfpal.report.entity.Client;
import com.jfpal.report.entity.Report;
import com.jfpal.report.entity.retrunMsg.NoticeRetunModel;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.jfpal.report.service.ReceiveReportService;
import com.jfpal.report.util.AffairQueueUtil;
import com.jfpal.report.util.AllocateEngine;
import com.jfpal.report.util.InformNoticeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("autoTask")
public class AutoAllocateTask {


    @Value("${spring.value.count}")
    private long count;

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(AutoAllocateTask.class);

    @Autowired
    private ReportMapperImpl reportMapperImpl;

    @Autowired
    private AllocateEngine allocateEngine;

    @Autowired
    private InformNoticeUtil informNoticeUtil;

    @Autowired
    private AffairQueueUtil affairQueueUtil;

    public void runJob() throws Exception {
        List<String> reportList = affairQueueUtil.getAffairList(count);
        if(reportList==null||reportList.size()==0){
            logger.info("任务队列中没有任务");
        }
        for (String s : reportList) {
            try {
                Report report= JsonUtil.parse(s, new TypeReference<Report>() {});
                synchronized (AllocateEngine.lockObject) {
                    List<Client> uselist = allocateEngine.getUsefulClients(1);
                    Client client = allocateEngine.allocate(uselist, report);
                    if (client != null) {
                        List<Client> list = allocateEngine.getUsefulClients(0);
                        allocateEngine.getClientByIp(list, client.getSystemIp());
                        list.add(client);
                        Collections.sort(list);
                        allocateEngine.putClientList(list);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//                MapUtil.transMap2Bean(map, report);
//                report.setStatus(CoreConstants.REPORT_FAIL);
//                report.setMessage(CoreConstants.REPORT_FAIL_MSG);
//                reportMapperImpl.updateReport(report);
//                NoticeRetunModel model = informNoticeUtil.noticeReportAndResponse(report);
            }
        }
    }

}
