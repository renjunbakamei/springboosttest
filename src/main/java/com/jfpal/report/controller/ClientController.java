/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/13.
 
 */
package com.jfpal.report.controller;

import com.jfpal.constants.RequestEnum;
import com.jfpal.core.utils.ResultMessage;
import com.jfpal.report.entity.Client;
import com.jfpal.report.service.ClientService;
import com.jfpal.report.util.AllocateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/report-client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AllocateEngine allocateEngine;

    /**
     *添加客户端信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="addClient",method = RequestMethod.POST)
    @ResponseBody
    public ResultMessage addClient(@RequestBody String param, HttpServletRequest request, HttpServletResponse response){
        ResultMessage rm=new ResultMessage();
        try {
//            String ip=(String)request.getParameter("ip");
            String ip=request.getRemoteAddr();
            String port=(String)request.getParameter("port");
            System.out.println(param);
            if(ip==null||port==null){
                rm.setCode(RequestEnum.CODE_NOTNULL.getRequestCode());
                rm.setMessage(RequestEnum.CODE_NOTNULL.getRequestReason()+"ip,port");
                return rm;
            }
            List<Client> list=allocateEngine.getUsefulClients(0);
            if(list==null){
                list=new ArrayList<Client>();
            }
            Client client=new Client(ip,port);
            Client clientOld=clientService.getClient(client);

            if(clientOld==null){
                Integer id=clientService.getSeqId();
                client.setSystemId(id);
                clientService.insertClient(client);
            }
            else{
                client=clientOld;
            }
            Client clientReids=allocateEngine.getClientByIp(list,ip);
            client.setCount(0);
            client.setSystomStatus(true);
            list.add(client);
            Collections.sort(list);
            allocateEngine.putClientList(list);
            rm.setCode(RequestEnum.CODE_SUCCESS.getRequestCode());
            rm.setMessage(RequestEnum.CODE_SUCCESS.getRequestReason());
        } catch (Exception e) {
            e.printStackTrace();
            rm.setCode(RequestEnum.CODE_ERROR.getRequestCode());
            rm.setMessage(RequestEnum.CODE_ERROR.getRequestReason());
        }
        return rm;
    }
}
