/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/12.
 
 */
package com.jfpal.report.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jfpal.constants.CoreConstants;
import com.jfpal.core.base.RedisClient;
import com.jfpal.core.utils.HttpClientUtil;
import com.jfpal.core.utils.JsonUtil;
import com.jfpal.report.entity.Client;
import com.jfpal.report.entity.Report;
import com.jfpal.report.entity.retrunMsg.NoticeRetunModel;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AllocateEngine {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(AllocateEngine.class);


    @Autowired
    private RedisClient redisClient;

    @Autowired
    private AffairQueueUtil affairQueueUtil;

    @Autowired
    private ReportMapperImpl reportMapperImpl;

    @Value("${allocate.maxCount}")
    private Long maxCount;

    @Value("${allocate.uri}")
    private String uri;

    @Value("${allocate.clientsKey}")
    private String clientsKey;

    @Value("${allocate.reportUri}")
    private String reportUri;

    @Autowired
    private InformNoticeUtil informNoticeUtil;

    public static Object lockObject = new Object();

    /**
     *查找全部可用服务器
     * @param all 查询条件，全部服务器或者可用服务器（0，查找全部服务器，其他查找可用服务器）
     * @return
     * @throws Exception
     */
    public List<Client> getUsefulClients(int all) throws Exception {
        String clients = redisClient.get(clientsKey);
        if (clients==null){
            return null;
        }
        List<Client> list= JsonUtil.parse(clients, new TypeReference<List<Client>>() {});
        if(0==all){
            return list;
        }else {
            if (list != null && list.size() > 0) {
                Client c = null;
                for (int i = 0; i < list.size(); i++) {
                    c = list.get(i);
                    if (!c.isSystomStatus()) {
                        list.remove(i);
                    }
                }
            }
            return list;
        }
    }

    /**
     *返回分配的服务器
     * @param list
     * @param count
     * @return
     */
    public Client getReadyClient(List<Client> list,long count){
        if(list==null||list.size()==0){
            return null;
        }
        Client client=new Client();
        if(count>=maxCount){
            client=list.get(0);
            client.setSystomStatus(false);
            return client;
        }
        for(int i=0;i<list.size();i++){
            client=list.get(i);
            if (client.getCount() + count < maxCount) {
                return client;
            }
        }
        client=list.get(0);
        client.setSystomStatus(false);
        return client;
    }

    /**
     * 根据ip地址查询查询client
     */
    public Client getClientByIp(List<Client> list,String ip){
        if(list==null||list.size()==0){
            return null;
        }
        Client c=new Client();
        for(int i=0;i<list.size();i++){
            c=list.get(i);
            if(c.getSystemIp().equals(ip)){
                list.remove(i);
                return c;
            }
        }
        return null;
    }

    /**
     *分配任务
     * @param c
     * @param id
     * @return
     */
    public boolean allocateAffair(Client c,int id) throws Exception{
        if(c==null) {
            throw new Exception("没有服务器地址可供生成报表");
        }
        String  url = "http://" + c.getSystemIp() + ":" + c.getSystomPort() + reportUri;
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("id",id);
//        String requestJson=JsonUtil.toJson(params);
        //TODO 添加接口信息
        try {
            String result= HttpClientUtil.sendPost(url,params);
            logger.info(result);
            Map<String,Object> returnParam=JsonUtil.parse(result, new TypeReference<Map<String, Object>>() {});
            String responseCode=(String)returnParam.get("resp_code");
            Report report=new Report();
            report.setId(id);
            if(CoreConstants.REPORT_CODE_NOTNULL.equals(responseCode)||CoreConstants.REPORT_CODE_NOID.equals(responseCode)){
                report.setStatus(CoreConstants.REPORT_FAIL);
                report.setResponseCodeReportServer(responseCode);
                reportMapperImpl.updateReportByid(report);
                return false;
            }else {
                report.setResponseCodeReportServer(responseCode);
                reportMapperImpl.updateReportByid(report);
                if(CoreConstants.REPORT_CODE_NOTUNDOING.equals(responseCode)){
                    return false;
                }else{
                    return true;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *添加机器信息到redis
     * @param list
     * @throws Exception
     */
    public void putClientList(List<Client> list) throws Exception {
        if(list!=null&&list.size()>0){
            Collections.sort(list);
        }
        String json=JsonUtil.toJson(list);
        redisClient.set(clientsKey,json);
    }

    /**
     *分配任务，当没有可分配机器时候，将任务放置在任务队列中
     * @param report
     * @throws Exception
     */

    public Client allocate(List<Client> clients,Report report) throws Exception{
        if(report==null){
            throw new Exception("被分配任务为空");
        }

        Client usefulClient=this.getReadyClient(clients,Long.parseLong(report.getReportCount()));
        if(usefulClient==null){
            logger.info("对于id为"+report.getId()+"的报表生成请求当前无可提供服务的机器，请等待");
            long long1=affairQueueUtil.putAffair(String.valueOf(report.getId()));
            return null;
        }
        Long count=usefulClient.getCount();
        count+=Long.parseLong(report.getReportCount());
        if(count>maxCount&&usefulClient.getCount()!=0){
            usefulClient.setSystomStatus(false);
            affairQueueUtil.putHead(String.valueOf(report.getId()));
            logger.info("对于id为"+report.getId()+"的报表生成请求当前无可提供服务的机器，请等待");
        }else{
            usefulClient.setCount(count);
//        usefulClient.setCount(Long.parseLong(report.getReportCount()));
            try{
                boolean result=this.allocateAffair(usefulClient,report.getId());
                if(!result) {
                    NoticeRetunModel model = informNoticeUtil.noticeReportAndResponse(report);
                    return null;
                }
                report.setServerUrl(usefulClient.getSystemIp());
                reportMapperImpl.updateReportServerUrl(report);
                logger.info("分配任务id" +report.getId()+"到"+usefulClient.getSystemIp()+"，总件数量："+usefulClient.getCount());
            }catch (Exception e){
                e.printStackTrace();
                for(int i=0;i<clients.size();i++){
                    if(usefulClient.getSystemId()==clients.get(i).getSystemId()){
                        clients.remove(i);
                        break;
                    }
                }
                usefulClient= allocate(clients,report);
            }
        }
        return usefulClient;
    }

    /**
     *分配任务，指定机器分配任务
     * @param report
     * @param client
     * @throws Exception
     */
    public Client allocate(Report report,Client client)throws Exception{
        if(client==null){
            logger.info("对于id为"+report.getId()+"的报表生成请求当前无可提供服务的机器，请等待");
            affairQueueUtil.putHead(String.valueOf(report.getId()));
            return null;
        }
        Long count=client.getCount();
        count+=Long.parseLong(report.getReportCount());

        if(count>maxCount){
            client.setSystomStatus(false);
            affairQueueUtil.putHead(String.valueOf(report.getId()));
        }else {
            client.setCount(Long.parseLong(report.getReportCount()));
            client.setSystomStatus(false);
            boolean result = this.allocateAffair(client, report.getId());
            if (!result) {
                NoticeRetunModel model = informNoticeUtil.noticeReportAndResponse(report);
                return null;
            }
            report.setServerUrl(client.getSystemIp());
            reportMapperImpl.updateReportServerUrl(report);
            logger.info("分配任务id" + report.getId() + "到" + client.getSystemIp());
        }
        return client;
    }

}
