/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/11.
 
 */
package com.jfpal.report.service;


import com.jfpal.constants.CoreConstants;
import com.jfpal.core.utils.MapUtil;
import com.jfpal.report.entity.Client;
import com.jfpal.report.entity.Report;
import com.jfpal.report.entity.retrunMsg.NoticeRetunModel;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.jfpal.report.util.AllocateEngine;
import com.jfpal.report.util.InformNoticeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ReceiveReportService {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(ReceiveReportService.class);

    @Autowired
    private ReportMapperImpl reportMapperImpl;

    @Autowired
    private AllocateEngine allocateEngine;

    @Autowired
    private InformNoticeUtil informNoticeUtil;

    @Async
    public void receiveJob(Map<String,Object> map){
        logger.info("报表生成："+map.get("userIds")+",报表名称:"+map.get("reportName"));
//        String sql=(String)map.get("sql");
//        String userId=(String)map.get("userIds");
        int rid= reportMapperImpl.getSeqId();
        Report report=new Report();
        report.setId(rid);
        try{
//            report.setSql(sql);
            //检查数据库有没有相同sql的数据
//            Report resultReport=reportService.getReport(report);
//            if(resultReport!=null){
//                if(CoreConstants.REPORT_READY.equals(resultReport)||CoreConstants.REPORT_ING.equals(resultReport)){
//                    logger.info("申请报表正在生成中");
//                    StringBuffer ids=new StringBuffer(report.getUserIds()).append(",").append(id);
//                    report.setUserIds(ids.toString());
//                    report.setStatus(null);
//                    report.setReportPath(null);
//                    reportService.updateReportByid(report);
//                    return;
//                }
//                //有相同的sql报表
//                //TODO通知消息系统
//                else {
//                    NoticeRetunModel model = informNoticeUtil.noticeReport(report);
//                    return;
//                }
//            }
            //数据库中没有相同sql的报表
            MapUtil.transMap2Bean(map,report);
            report.setStatus("00");
//            report.setUserIds(userId);
            reportMapperImpl.updateReport(report);
//            String result=allocateEngine.allocateAffair(null,20);
            synchronized(AllocateEngine.lockObject) {
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
        }catch (Exception e){
            e.printStackTrace();
            MapUtil.transMap2Bean(map,report);
            report.setStatus(CoreConstants.REPORT_FAIL);
            report.setMessage(CoreConstants.REPORT_FAIL_MSG);
            reportMapperImpl.updateReport(report);
            NoticeRetunModel model=informNoticeUtil.noticeReportAndResponse(report);
        }
    }

}
