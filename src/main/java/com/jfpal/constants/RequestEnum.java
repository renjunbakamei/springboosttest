/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/11.
 
 */
package com.jfpal.constants;

import com.github.pagehelper.util.StringUtil;
import org.springframework.util.StringUtils;

public enum RequestEnum {

    CODE_ERROR("系统异常!","RP99"),
    CODE_SUCCESS("处理成功!","0000"),
    CODE_FAIL("处理失败!","RP01"),
    CODE_NOTNULL("处理失败，缺少必填参数:","RP02");

    private String requestReason,requestCode;

    private RequestEnum(String reason,String code){
        this.requestCode=code;
        this.requestReason=reason;
    }

    //根据返回码查询返回原因
    public static String getReason(String code){
        if(StringUtils.isEmpty(code)){
            throw new NullPointerException("返回码不能为空");
        }
        else{
            for(RequestEnum r:RequestEnum.values()) {
                if (r.getRequestCode().equals(code)) {
                    return r.requestReason;
                }
            }
        }
        return null;
    }


    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }
    @Override
    public String toString(){
        StringBuffer sb=new StringBuffer();
        sb.append("返回码：").append(this.getRequestCode()).append(";返回码描述:")
                .append(this.getRequestReason());
        return sb.toString();
    }
}
