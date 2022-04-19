package com.xie.crm.commons.domain;

public class ReturnObject {
    //登录的提示信息，0为失败，1为成功
    private String code;
    //对应的提示信息
    private String message;
    //考虑到有其它类型的信息需要返回，定义一个Object对象
    private Object retData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
