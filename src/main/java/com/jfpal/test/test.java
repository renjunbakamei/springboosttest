/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/18.
 
 */
package com.jfpal.test;

import com.jfpal.core.utils.HttpClientUtil;
import com.jfpal.core.utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test {
    public static void main(String[] args) {
//        try {
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("type","2");
//            params.put("reportName", "payjnlsReport2");
//            params.put("sql", "select p.servcode,p.tradecode,p.servcode ||'-'|| jfpay.srvc_name(p.servcode) servname,p.tradecode  ||'-'|| jfpay.trd_name(p.tradecode) tradename,p.localdate,p.localtime,p.locallogno,p.localsettlmtdate,p.customerid,p.channelid,p.termid,p.psamid,p.branchid,p.orgtxlogno,p.orgtxrefno,p.orgtxdate,p.orgtxtime,p.orgsettlmtdate,p.sndtxlogno,p.sndtxdate,p.sndtxtime,p.account,p.account2,p.amount,p.fee,p.hostbranchid,p.hostmerchid,p.hosttermid,p.hostsettlmtdate,p.hostlogno,p.hostdate,p.hosttime,p.hostrefno,p.hostauthcode,p.issuer,p.issuer2,p.msgcode,p.hostmsgcode,p.msgtext,p.status,p.paytag,p.checkcode,p.remark,p.mobileno,p.merchantid,p.productid,p.appuser,p.blank3 from jfpay.paycxjnls p where 1=1");
//            params.put("userIds","131");
//            params.put("reportType","xls");
//            params.put("reportCount","1094");
//            params.put("reportTitle","参数1|参数2|参数3|参数4|参数5|参数6|参数7|参数8|参数9|参数10|参数11|参数12|参数13|参数14|参数15|参数16|参数17|参数18|参数19|参数20|参数21|参数22|参数23|参数24|参数25参数26|参数27|参数28|参数29|参数30|参数31|参数32|参数33|参数34|参数35|参数36|参数37|参数38|参数39|参数40|参数41|参数42|参数43|参数44|参数45|参数46|参数47|参数48|参数49|");
//            String json = JSONObject.toJSONString(params);
//            String result = null;
//            result = HttpClientUtil.sendPost("http://192.180.2.51:8081/report-service/inform", json.toString());
//            System.out.println("inform:" + result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

            ExecutorService pool = Executors. newCachedThreadPool();
            for (int i=0;i<500;i++) {
                pool.execute(new Runnable(){
                    @Override
                    public void run() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("type", "2");
                        params.put("reportName", "payjnlsReport2");
                        params.put("sql", "select p.servcode,p.tradecode,p.servcode ||'-'|| jfpay.srvc_name(p.servcode) servname,p.tradecode  ||'-'|| jfpay.trd_name(p.tradecode) tradename,p.localdate,p.localtime,p.locallogno,p.localsettlmtdate,p.customerid,p.channelid,p.termid,p.psamid,p.branchid,p.orgtxlogno,p.orgtxrefno,p.orgtxdate,p.orgtxtime,p.orgsettlmtdate,p.sndtxlogno,p.sndtxdate,p.sndtxtime,p.account,p.account2,p.amount,p.fee,p.hostbranchid,p.hostmerchid,p.hosttermid,p.hostsettlmtdate,p.hostlogno,p.hostdate,p.hosttime,p.hostrefno,p.hostauthcode,p.issuer,p.issuer2,p.msgcode,p.hostmsgcode,p.msgtext,p.status,p.paytag,p.checkcode,p.remark,p.mobileno,p.merchantid,p.productid,p.appuser,p.blank3 from jfpay.paycxjnls p where 1=1");
                        params.put("userIds", "131");
                        params.put("reportType", "xls");
                        params.put("reportCount", "1094");
                        params.put("reportTitle", "参数1|参数2|参数3|参数4|参数5|参数6|参数7|参数8|参数9|参数10|参数11|参数12|参数13|参数14|参数15|参数16|参数17|参数18|参数19|参数20|参数21|参数22|参数23|参数24|参数25参数26|参数27|参数28|参数29|参数30|参数31|参数32|参数33|参数34|参数35|参数36|参数37|参数38|参数39|参数40|参数41|参数42|参数43|参数44|参数45|参数46|参数47|参数48|参数49|");
                        params.put("systemId","PM");
                        params.put("userIds","131");
                        String json = JsonUtil.toJson(params);
                        String result = null;
                        System.out.println(json);
                        try {
                            result = HttpClientUtil.sendPost("http://192.180.2.51:8081/report-service/inform",json);
                            System.out.println(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
    }
}
