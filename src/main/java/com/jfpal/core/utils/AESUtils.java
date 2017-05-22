/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/5/10.
 
 */
package com.jfpal.core.utils;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 编码工具类
 * 1.将byte[]转为各种进制的字符串
 * 2.base 64 encode
 * 3.base 64 decode
 * 4.获取byte[]的md5值
 * 5.获取字符串md5值
 * 6.结合base64实现md5加密
 * 7.AES加密
 * 8.AES加密为base 64 code
 * 9.AES解密
 * 10.将base 64 code AES解密
 *
 *
 */
public class AESUtils {


    /**
     *将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return
     */
    public static String binary(byte[] bytes,int radix){
        return new BigInteger(1,bytes).toString(radix);
    }

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code)?null:new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     *  获取byte[]的md5值
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) throws Exception{
        MessageDigest md=MessageDigest.getInstance("MD5");
        md.update(bytes);
        return md.digest();
    }

    /**
     * 获取字符串md5值
     * @param msg
     * @return
     * @throws Exception
     */
    public static byte[] md5(String msg) throws Exception{
        return StringUtils.isEmpty(msg)?null:md5(msg.getBytes());
    }

    /**
     * 结合base64实现md5加密
     * @param msg 待加密字符串
     * @return 获取md5后转为base64
     * @throws Exception
     */
    public static String md5Encrypt(String msg) throws Exception{
        return StringUtils.isEmpty(msg)?null:base64Encode(md5(msg));
    }

    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content,String encryptKey) throws Exception{
        KeyGenerator kgen=KeyGenerator.getInstance("AES");
        kgen.init(128,new SecureRandom(encryptKey.getBytes()));

        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(kgen.generateKey().getEncoded(),"AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String content,String encryptKey) throws Exception{
        return base64Encode(aesEncryptToBytes(content,encryptKey));
    }

    /**
     * AES解密
     * @param encryptBytes  待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes,String decryptKey) throws Exception{
        KeyGenerator kgen=KeyGenerator.getInstance("AES");
        kgen.init(128,new SecureRandom(decryptKey.getBytes()));

        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(kgen.generateKey().getEncoded(),"AES"));
        byte[] decryptBytes=cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr,String decryptKey) throws Exception{
        return StringUtils.isEmpty(encryptStr)?null:aesDecryptByBytes(base64Decode(encryptStr),decryptKey);
    }

    public static void main(String args[]){
        try{
            String content="select p.servcode,p.tradecode,p.servcode ||'-'|| jfpay.srvc_name(p.servcode) servname,p.tradecode  " +
                    "||'-'|| jfpay.trd_name(p.tradecode) tradename,p.localdate,p.localtime,p.locallogno,p.localsettlmtdate,p.customerid," +
                    "p.channelid,p.termid,p.psamid,p.branchid,p.orgtxlogno,p.orgtxrefno,p.orgtxdate,p.orgtxtime,p.orgsettlmtdate," +
                    "p.sndtxlogno,p.sndtxdate,p.sndtxtime,p.account,p.account2,p.amount,p.fee,p.hostbranchid,p.hostmerchid,p.hosttermid," +
                    "p.hostsettlmtdate,p.hostlogno,p.hostdate,p.hosttime,p.hostrefno,p.hostauthcode,p.issuer,p.issuer2,p.msgcode," +
                    "p.hostmsgcode,p.msgtext,p.status,p.paytag,p.checkcode,p.remark,p.mobileno,p.merchantid,p.productid,p.appuser," +
                    "p.blank3 from jfpay.paycxjnls p where 1=1";
            System.out.println("加密前:"+content);
            String key="123456";
            System.out.println("密钥:"+key);
            String encrypt=aesEncrypt(content,key);
            System.out.println("加密后:"+encrypt);
            String decrypt = aesDecrypt(encrypt, key);
            System.out.println("解密后：" + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
