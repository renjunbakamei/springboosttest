/*
 * Copyright 2017 jfpal.com All right reserved. This software is the
 * confidential and proprietary information of jfpal.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jfpal.com.
 
 Created by jun.ren on 2017/4/11.
 
 */
package com.jfpal.core.exception;

public class BizException extends Exception {

    private String errorCode;
    private String showMessage;
    private Throwable cause;

    public BizException(){}

    public BizException(String message){super(message);}

    public BizException(String errorCode,String message){
        super(message);
        this.errorCode=errorCode;
    }

    public BizException(String message,Throwable cause){
        super(message,cause);
        this.cause=cause;
    }

    public BizException(String errorCode,String message,Throwable cause){
        super(message,cause);
        this.errorCode=errorCode;
        this.cause=cause;
    }

    public String getMessage(){
        String msg=super.getMessage();
        if(this.errorCode!=null){
            if(msg!=null) {
                msg = "[error_code:" + this.errorCode + "] " + msg;
            }
			else {
                    msg = "[error_code:" + this.errorCode + "]";
                }
        }
        String causeMsg = null;
        if (this.cause != null) {
            causeMsg = this.cause.getMessage();
        }
        if (msg != null) {
            if (causeMsg != null) {
                return msg + " caused by: " + causeMsg;
            }
            return msg;
        }

        return causeMsg;
    }

    public BizException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public String toString() {
        if (this.cause == null) {
            return super.toString();
        }
        return super.toString() + " cause: " + this.cause.toString();
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getShowMessage() {
        if (this.showMessage == null)
            return super.getMessage();
        return this.showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

}
