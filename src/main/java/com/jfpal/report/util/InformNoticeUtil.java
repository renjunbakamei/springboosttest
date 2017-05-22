/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/14.
 
 */
package com.jfpal.report.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jfpal.constants.CoreConstants;
import com.jfpal.core.utils.JsonUtil;
import com.jfpal.core.utils.SocketUtil;
import com.jfpal.report.entity.Report;
import com.jfpal.report.entity.retrunMsg.NoticeRetunModel;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class InformNoticeUtil {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(InformNoticeUtil.class);

    @Value("${allocate.noticeHost}")
    private String host;

    @Value("${allocate.noticePort}")
    private Integer port;

    @Value("${allocate.content}")
    private String content;
    @Value("${allocate.contentFail}")
    private String contentFail;

    @Autowired
    private ReportMapperImpl reportMapperImpl;

    public NoticeRetunModel notice(String content){
        SocketUtil client = new SocketUtil(host,port);
        String returnResult = client.sendSocketMsg(content);
        NoticeRetunModel model= JsonUtil.parse(returnResult, new TypeReference<NoticeRetunModel>() {});
        return model;
    }

    /**
     *组装消息通知内
     * @return
     */
    public String makeContent(Report report){
        String path=report.getReportPath();
        return String.format(content,report.getReportName(),path,path.substring(path.lastIndexOf("/")+1));
    }

    /**
     *通知结果
     * @param r
     * @return
     */
    public NoticeRetunModel noticeReport(Report r){


        Map<String,Object> params=new HashMap<String,Object>();
        params.put("REQ_TYPE","NOTICE_ADDBYIDS");
        params.put("type","01");
        params.put("ids",r.getUserIds());
        if(CoreConstants.REPORT_OVER.equals(r.getStatus())) {
            String content = makeContent(r);
            params.put("content", content);
        } else if (CoreConstants.REPORT_FAIL.equals(r.getStatus())) {
            params.put("content", contentFail);
        }else{
            return null;
        }
        String json=JsonUtil.toJson(params);
        logger.info("发送消息通知系统报文:"+json);
        NoticeRetunModel model=notice(json);
        return notice(json);
    }

    /**
     *通知结果并将返回结汇返回数据库
     * @param r
     * @return
     */
    public NoticeRetunModel noticeReportAndResponse(Report r){

        NoticeRetunModel model=noticeReport(r);
        logger.info("消息系统返回报文："+model.toString(model));
        Report report=new Report();
        report.setId(r.getId());
        report.setReponseCode(model.getHEAD().get("RETURN_CODE"));
        int updateCount= reportMapperImpl.updateReportByid(report);
        return model;
    }
}
