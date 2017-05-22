/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/12.
 
 */
package com.jfpal.report.entity;


import com.jfpal.core.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="report_client")
public class Client extends BaseEntity implements Comparable{


    /**
     *系统id
     */
    @Id
    @Column(name="system_Id")
    private Integer systemId;

    /**
     *系统ip
     */
    @Column(name="system_Ip")
    private String systemIp;

    /**
     *系统端口
     */
    @Column(name="system_Port")
    private String systomPort;

    /**
     *该服务器总执行条数
     */
    @Column(name="system_count")
    private long count;

    /**
     *服务器状态（启用或停用）
     */
    @Transient
    private boolean systomStatus;

    /**
     *可用线程数
     */
    @Column(name="threadCount")
    private String threadCount;

    public Integer getSystemId() {
        return systemId;
    }

    public Client(){}

    public Client(String ip,String port){
        this.systemIp=ip;
        this.systomPort=port;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getSystemIp() {
        return systemIp;
    }

    public void setSystemIp(String systemIp) {
        this.systemIp = systemIp;
    }

    public String getSystomPort() {
        return systomPort;
    }

    public void setSystomPort(String systomPort) {
        this.systomPort = systomPort;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isSystomStatus() {
        return systomStatus;
    }

    public void setSystomStatus(boolean systomStatus) {
        this.systomStatus = systomStatus;
    }

    public String getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public int compareTo(Object o) {
        Client c=(Client)o;
        return new Long(this.getCount()).compareTo(new Long(c.getCount()));
    }
}
