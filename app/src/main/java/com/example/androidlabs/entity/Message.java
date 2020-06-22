package com.example.androidlabs.entity;

public class Message {

    private String msg;

    private String avator;

    public Message(String msg, String avator) {
        this.msg = msg;
        this.avator = avator;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }
}
