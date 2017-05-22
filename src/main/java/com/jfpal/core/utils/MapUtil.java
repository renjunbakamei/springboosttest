/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/11.
 
 */
package com.jfpal.core.utils;

import com.jfpal.constants.RequestEnum;
import com.jfpal.core.exception.BizException;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MapUtil {

    /**
     *根据传入字段判断map中是否存在
     */
    public static void checkMapByKey(Map<String,Object> map,String... params) throws BizException{

        StringBuffer sb=new StringBuffer();
        for(String s:params){
            if(!map.containsKey(s)){
                sb.append(s).append(",");
                continue;
            }
            else{
                if(null==map.get(s)|| StringUtils.isEmpty(map.get(s))){
                    sb.append(s).append(",");
                    continue;
                }
            }
        }
        String returnMsg=sb.toString();
        if(!StringUtils.isEmpty(returnMsg)){
            throw new BizException(RequestEnum.CODE_NOTNULL.getRequestCode(),
                    RequestEnum.CODE_NOTNULL.getRequestReason()+returnMsg.substring(0,returnMsg.length()-1));
        }
    }

    /**
     *讲map转为javabean
     * @param obj
     * @param map
     * @return
     *
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }

            }

        } catch (Exception e) {
            System.out.println("transMap2Bean Error " + e);
        }

        return;

    }

}
