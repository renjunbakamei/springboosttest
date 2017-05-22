/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/7.
 
 */
package com.jfpal.report.controller;

import com.github.pagehelper.PageInfo;
import com.jfpal.constants.CoreConstants;
import com.jfpal.constants.RequestEnum;
import com.jfpal.core.base.RedisClient;
import com.jfpal.core.exception.BizException;
import com.jfpal.core.utils.JsonUtil;
import com.jfpal.core.utils.MapUtil;
import com.jfpal.core.utils.ResultMessage;
import com.jfpal.report.entity.Report;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.jfpal.report.service.ReceiveReportService;
import com.jfpal.report.service.ReportHandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report-service")
public class ReportController {

    final static Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportMapperImpl reportMapperImpl;

    @Autowired
    private ReportHandleService reportHandleService;

    @Autowired
    private ReceiveReportService receiveReportService;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value="test")
    public PageInfo<Report> getAll(){
        Report report=new Report();
        List<Report> list= reportMapperImpl.getAll(report);
        if(list.size()>0)
        {
            System.out.println(list.get(0).toString());
        }
        return new PageInfo<Report>(list);
    }

    @RequestMapping(value="test2",method = RequestMethod.POST)
    @ResponseBody
    public String test2 (@RequestBody String key, HttpServletRequest request,
                         HttpServletResponse response) throws Exception
    {

        return redisClient.get(key);
    }

    @RequestMapping(value = "/inform",method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResultMessage gainReportStatus(@RequestBody String param, HttpServletRequest request,
                                          HttpServletResponse response){
        ResultMessage rm=new ResultMessage();
        try{
            param= URLDecoder.decode(param,"utf-8");
            logger.info("请求信息:"+param);
            Map<String,Object> paramMap= JsonUtil.parse(param,JsonUtil.MAP_REFERENCE);
//            if(!paramMap.containsKey("id")){
//                rm.setCode(RequestEnum.CODE_FAIL.getRequestCode());
//                rm.setMessage(RequestEnum.CODE_FAIL.getRequestReason());
//            }
            MapUtil.checkMapByKey(paramMap,"type");
            String type=(String)paramMap.get("type");
            if(CoreConstants.GAINREPORTSTATUS.equals(type)) {
                logger.info("报表完成通知:客户端ip" + request.getRemoteAddr());
                String ip=request.getRemoteAddr();
                MapUtil.checkMapByKey(paramMap,"id","report_sever_ip");
                reportHandleService.informReportOver(Integer.parseInt(String.valueOf(paramMap.get("id"))),ip);
            }
            if(CoreConstants.RECEIVEPMJOB.equals(type)){
                MapUtil.checkMapByKey(paramMap,"reportName", "sql","reportType",
                        "reportTitle","reportCount","userIds","systemId");
                logger.info("接受报表任务");
                receiveReportService.receiveJob(paramMap);
            }
            rm.setCode(RequestEnum.CODE_SUCCESS.getRequestCode());
            rm.setMessage(RequestEnum.CODE_SUCCESS.getRequestReason());
        }catch (BizException be)
        {
            rm.setCode(be.getErrorCode());
            rm.setMessage(be.getShowMessage());
            be.printStackTrace();
        }
        catch (Exception e){
            rm.setCode(RequestEnum.CODE_ERROR.getRequestCode());
            rm.setMessage(RequestEnum.CODE_ERROR.getRequestReason());
            e.printStackTrace();
        }
        return rm;
    }

}
