/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/13.
 
 */
package com.jfpal.report.mapper;

import com.jfpal.core.utils.MapperHelper;
import com.jfpal.report.entity.Client;

public interface ClientMapper extends MapperHelper<Client> {

    public void insertReportClient(Client client);

    public int selectSeqReportClient();

}
