package com.example.androidlabs.entity;

public class Message {

    private long id;

    private String msg;

    private boolean isSent ;

    public Message(long id, String msg, boolean isSent) {
        this.id = id;
        this.msg = msg;
        this.isSent = isSent;
    }


    public Message(String msg, boolean isSent) {
       this(0,msg,isSent);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
