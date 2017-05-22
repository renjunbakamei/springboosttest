package com.jfpal.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 类HttpClientUtil.java的实现描述：TODO 类实现描述
 * @author zhc 2017年1月13日 下午5:41:10
 */
public class HttpClientUtil {

    final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);


    public static String sendPost(String url, Map<String, Object> params) throws IOException   {



        String urlParameters = "";

        for (Map.Entry<String, Object> param : params.entrySet()) {
            System.out.println("Key = " + param.getKey() + ", Value = " + param.getValue());
            String tmpParam = param.getKey() + "=" + URLEncoder.encode(param.getValue().toString(), "UTF-8") + "&";
            urlParameters += tmpParam;
        }
        urlParameters = urlParameters.substring(0, (urlParameters.length() - 1));
        return  sendPost(url,urlParameters);
    }

    public static String sendPost(String url, String params) throws IOException   {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Charset", "UTF-8");
        con.setRequestProperty("contentType", "application/json;charset=UTF-8");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

//    public static void main(String[] args){
//        Map<String,Object> map=new HashMap<String, Object>();
//        map.put("uid","uid");
//        map.put("localDateBegin","20170201");
//        map.put("currentPageNum","1");
//        map.put("localDateEnd","20170224");
//        map.put("cid","pm");
//        map.put("limtNo","20");
//        String url="http://192.168.1.104:8081/pm-core/restful/payjnls/historyPayjnlsList";
//        try {
//            String result=sendPost(url,map);
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}

