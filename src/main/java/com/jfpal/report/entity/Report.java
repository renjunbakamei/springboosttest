/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/7.
 
 */
package com.jfpal.report.entity;



import com.jfpal.core.base.BaseEntity;

import javax.persistence.*;

@Table(name="report_order")

public class Report extends BaseEntity {

    @Id
    @SequenceGenerator(name="ID_SEQ_REPORT",sequenceName = "seq_report_order",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "ID_SEQ_REPORT")
    private Integer id;

    @Column(name="REPORT_NAME")
    private String reportName;

    @Column(name="REPORT_PATH")
    private String reportPath;

    @Column(name="REPORT_SQL")
    private String sql;
    @Column(name="STATUS")
    private String status;

    @Column(name="REPORT_MSG")
    private String message;

    @Column(name="SYSTEM_ID")
    private String systemId;

    @Column(name="REPORT_TYPE")
    private String reportType;

    @Column(name="REPORT_LEVEL")
    private String reportLevel;

    @Column(name="SERVER_URL")
    private String  serverUrl;

    @Column(name="REPORT_COUNT")
    private String reportCount;

    @Column(name="REPORT_TITLE")
    private String reportTitle;

    @Column(name="CREATE_TIME")
    private String createTime;

    @Column(name="UPDATE_TIME")
    private String updateTime;

    @Column(name="REPORT_SHEET_NAME")
    private String sheetName;

    @Column(name="USERIDS")
    private String userIds;

    @Column(name="REPONSE_CODE")
    private String reponseCode;


    @Column(name="RESPONSE_CODE_RS")
    private String responseCodeReportServer;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportLevel() {
        return reportLevel;
    }

    public void setReportLevel(String reportLevel) {
        this.reportLevel = reportLevel;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getReportCount() {
        return reportCount;
    }

    public void setReportCount(String reportCount) {
        this.reportCount = reportCount;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getUserIds() {
        return userIds;
    }

    public String getResponseCodeReportServer() {
        return responseCodeReportServer;
    }

    public void setResponseCodeReportServer(String responseCodeReportServer) {
        this.responseCodeReportServer = responseCodeReportServer;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getReponseCode() {
        return reponseCode;
    }

    public void setReponseCode(String reponseCode) {
        this.reponseCode = reponseCode;
    }

    @Override
    public String toString(){
        StringBuffer sb=new StringBuffer();
        sb.append("id=").append(this.getId()).append("||reportname=").append(this.getReportName());
        return sb.toString();
    }
}
