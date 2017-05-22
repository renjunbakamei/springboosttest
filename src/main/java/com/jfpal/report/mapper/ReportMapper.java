/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/7.
 
 */
package com.jfpal.report.mapper;

import com.jfpal.core.utils.MapperHelper;
import com.jfpal.report.entity.Report;

import java.util.List;

public interface ReportMapper extends MapperHelper<Report> {

    public void insertReportOrder(Report report);

    public int selectSeqReportOrder();

    public int updateReportOrderById(Report report);

    public List<Report> selectReportByStatus(int rowCount);

    public int updateReportServerUrl(Report report);

    public List<Report> select_ReportList(Report report);


}
