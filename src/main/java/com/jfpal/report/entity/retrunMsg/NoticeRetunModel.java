/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/14.
 
 */
package com.jfpal.report.entity.retrunMsg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jfpal.core.utils.JsonUtil;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticeRetunModel {

    @JsonProperty("HEAD")
    private Map<String, String> HEAD;

    @JsonProperty("BODY")
    private ReturnContent       BODY;

    public Map<String, String> getHEAD() {
        return HEAD;
    }

    public void setHEAD(Map<String, String> hEAD) {
        HEAD = hEAD;
    }

    public ReturnContent getBODY() {
        return BODY;
    }

    public void setBODY(ReturnContent bODY) {
        BODY = bODY;
    }


    public String toString(NoticeRetunModel model){
        if(model==null){
            return null;
        }
        String result= JsonUtil.toJson(model);
        return result;
    }

}
