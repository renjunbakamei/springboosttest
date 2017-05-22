/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/13.
 
 */
package com.jfpal.report.service;

import com.jfpal.core.utils.HttpClientUtil;
import com.jfpal.report.entity.Client;
import com.jfpal.report.mapper.ClientMapper;
import com.jfpal.report.util.AllocateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientMapper clientMapper;


    @Autowired
    private AllocateEngine allocateEngine;

    @Value("${allocate.queryUri}")
    private String checkUri;

    final String HTTPHEAD="http://";

    final String HTTPSEPERATOR="/";

    public List<Client> getAll(){
        return clientMapper.selectAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertClient(Client client) throws RuntimeException{
        try{
            this.clientMapper.insertReportClient(client);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("添加客户端信息异常!");
        }

    }

    public Client getClient(Client client){
        return clientMapper.selectOne(client);
    }

    public Integer getSeqId(){
        return clientMapper.selectSeqReportClient();
    }

    public void clientCogradient(List<Client> redisClient){
        try {
            List<Client> databaseClients = this.getAll();
            Client c = new Client();
            int i = 0;//临时变量i
            boolean re = true;//临时变量re
            for (Client client : databaseClients) {
                for (int j = i; j < redisClient.size(); j++) {
                    if (client.getSystemId() == redisClient.get(j).getSystemId()) {
                        re = false;
                        break;
                    }
                }
                if (!re) {
                    re = true;
                    continue;
                } else {
                    checkClient(client);
                    client.setCount(0);
                    redisClient.add(0, client);
                }
            }
            Collections.sort(redisClient);
            allocateEngine.putClientList(redisClient);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    String checkClient(Client client) throws IOException {
        StringBuffer checkUrl=new StringBuffer();
        checkUrl.append(HTTPHEAD).append(client.getSystemIp()).append(":").append(client.getSystomPort())
                .append(HTTPSEPERATOR).append(checkUri);
        String result= HttpClientUtil.sendPost(checkUrl.toString(),"");
        return result;
    }

}
