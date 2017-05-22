/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/7.
 
 */
package com.jfpal.report.mapper.impl;

import com.github.pagehelper.PageHelper;
import com.jfpal.report.entity.Report;
import com.jfpal.report.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportMapperImpl {

    @Autowired
    private ReportMapper reportMapper;

    /**
     * 根据条件查询全部数据
     * @param report
     * @return
     */
    public List<Report> getAll(Report report){
        if(report.getPage()!=null&&report.getRows()!=null){
            PageHelper.startPage(report.getPage(),report.getRows());
        }
        return reportMapper.selectAll();
    }

    /**
     * 根据id进行查询
     * @param id
     * @return
     */
    public Report getReport(int id){
        return reportMapper.selectByPrimaryKey(id);
    }


    /**
     * 插入report
     * @param report
     * @throws RuntimeException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateReport(Report report) throws RuntimeException{
        try{
            this.reportMapper.insertReportOrder(report);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("添加报表信息异常!");
        }

    }

    /**
     * 得到report_order表的最新id
     * @return
     */
    public Integer getSeqId(){
        return reportMapper.selectSeqReportOrder();
    }

    /**
     * 根据条件查询report
     * @param report 查询条件
     * @return
     */

    public Report getReport(Report report){
        return reportMapper.selectOne(report);
    }

    /**
     * 根据条件更新report_order表
     * @param report
     * @return
     */
    public int updateReportByid(Report report) {
        return reportMapper.updateReportOrderById(report);
    }

    /**
     * 根据条数查询信息
     * @param rowCount 需要查询的条数
     * @return
     */
    public List<Report> getListByReport(int rowCount){
        return reportMapper.selectReportByStatus(rowCount);
    }

    /**
     * 根据条件更新report_order的服务器url
     * @param report
     * @return
     */
    public int updateReportServerUrl(Report report){return reportMapper.updateReportServerUrl(report);}

    /**
     * 根据条件查询report_order列表
     * @param report
     * @return
     */
    public List<Report> getReportList(Report report){return reportMapper.select_ReportList(report);}





}
