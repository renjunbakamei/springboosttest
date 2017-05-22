/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/14.
 
 */
package com.jfpal.report.entity.retrunMsg;

import java.util.List;
import java.util.Map;

public class ReturnContent {

    private long                      totalCount;
    private List<Map<String, Object>> list;
    private Map<String, Object>       obj;
    private long                      effectCount;
    private String                    message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public Map<String, Object> getObj() {
        return obj;
    }

    public void setObj(Map<String, Object> obj) {
        this.obj = obj;
    }

    public long getEffectCount() {
        return effectCount;
    }

    public void setEffectCount(long effectCount) {
        this.effectCount = effectCount;
    }

}
