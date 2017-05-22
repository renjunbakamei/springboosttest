/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/13.
 
 */
package com.jfpal.report.util;

import com.jfpal.core.base.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AffairQueueUtil {
    @Autowired
    private RedisClient redisClient;

    @Value("${allocate.affairKey}")
    private String affairKey;

    /**
     *得到队列头任务
     * @return
     * @throws Exception
     */
    public String getHeadAffair() throws Exception{
        return redisClient.pop(affairKey);
    }

    /**
     *将新任务放在队列尾部
     * @param id
     * @return
     * @throws Exception
     */
    public Long putAffair(String id) throws Exception{
        return redisClient.pushTail(affairKey,id);
    }

    /**
     *将新任务放在队列头部
     * @param id
     * @return
     * @throws Exception
     */
    public Long putHead(String id) throws Exception{
        return redisClient.push(affairKey,id);
    }

    /**
     *得到全部任务
     * @return
     * @throws Exception
     */
    public List<String> getAffairList() throws Exception{
        return redisClient.getList(affairKey);
    }

    /**
     *得到任务队列中的前n个任务
     *
     * @return
     * @throws Exception
     */
    public List<String> getAffairList(long count) throws Exception{
        return redisClient.range(affairKey,0,count);
    }

}
