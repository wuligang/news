package com.hisoft.news.util;

import java.io.Serializable;

/**
 * @program: news
 * @description:
 * @author: wlg
 * @create: 2021-06-30 09:38:50
 **/
public class JsonResult implements Serializable {

    public static final int SUCCESS = 200;
    public static final int ERROR = 201;


    //返回成功或失败的状态码  成功：200   失败：非200，比如201
    private int state;
    //失败时展示的消息
    private String message;
    //成功时返回的数据
    private Object data;

    public JsonResult(int state, String message, Object data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    //成功时创建的对象
    public JsonResult(Object data) {
        this.state = SUCCESS;
        this.data = data;
        this.message = "";
    }

    //失败时创建的对象
    public JsonResult(Throwable e){
        this.state = ERROR;
        this.message = e.getMessage();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
