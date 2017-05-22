/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/14.
 
 */
package com.jfpal.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class SocketUtil {

    private static Logger logger = LoggerFactory.getLogger(SocketUtil.class);
    /**
     * 通讯地址
     */
    private String ip;
    /**
     * 通讯端口
     */
    private Integer port;

    public SocketUtil(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 发送与后端通讯信息
     *
     * @param msg
     * @return
     */
    public String sendSocketMsg(String msg) {
        InetAddress address = null;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            address = InetAddress.getLocalHost();
            socket = new Socket(ip, port);
            socket.setSoTimeout(90000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
            out.write(URLEncoder.encode(msg, "UTF-8") + "\r\n");
            out.flush();
            String reply = null;
            StringBuffer infoS = new StringBuffer("");
            while (!((reply = in.readLine()) == null)) {
                infoS.append(reply + "\n");
                if (reply.indexOf("^JFPAL***END^") > -1) {
                    logger.info("SocketClient.sendSocketMsg [数据读取结束]");
                    break;
                }
            }
            String returnInfo = infoS.substring(0, infoS.indexOf("^JFPAL***END^"));
            if (returnInfo == null || "".equals(returnInfo)) {
                logger.error("socket返回空:【" + msg + "】");
                return "";
            }
            return URLDecoder.decode(returnInfo.toString(), "utf-8");
        } catch (UnknownHostException e) {
            logger.error("SocketClient.sendSocketMsg [IP:{}, PORT:{} 通讯连接异常 Excetion = {}]",
                    new String[] { ip, String.valueOf(port), e.getMessage() }, e);
            return null;
        } catch (IOException e) {
            logger.error("SocketClient.sendSocketMsg [通讯数据:{} 交互异常 Excetion = {}]",
                    new String[] { msg, e.getMessage() }, e);
            return null;
        } catch (Exception e) {
            logger.error("SocketClient.sendSocketMsg [通讯数据:{} 系统异常 Excetion = {}]",
                    new String[] { msg, e.getMessage() }, e);
            return null;
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("SocketClient.sendSocketMsg [socket关闭异常:{} 系统异常 Excetion = {}]",
                        new String[] { msg, ex.getMessage() }, ex);
            }
            try {
                if(in!=null){
                    in.close();
                }
            }catch (Exception e){
            }
            try {
                if(out!=null){
                    out.close();
                }
            }catch (Exception e){
            }
            try {
                if(socket!=null){
                    socket.close();
                }
            }catch (Exception e){
                logger.error("SocketClient.sendSocketMsg [socket关闭异常:{} 系统异常 Excetion = {}]",
                        new String[] { msg, e.getMessage() }, e);
            }
        }
    }

}
