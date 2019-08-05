package com.smart.cloud.fire.activity.Inspection.NoticeListActivity;

import java.io.Serializable;

public class NoticeEntity implements Serializable{

    private long id;
    private String title;
    private String content;
    private String publisher;
    private String publishtime;
    private String nlevel;
    private String nlevelName;
    private String receiver;
    private String receiverName;
    private String alarmType;
    private String isback;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getNlevel() {
        return nlevel;
    }

    public void setNlevel(String nlevel) {
        this.nlevel = nlevel;
    }

    public String getNlevelName() {
        return nlevelName;
    }

    public void setNlevelName(String nlevelName) {
        this.nlevelName = nlevelName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getIsback() {
        return isback;
    }

    public void setIsback(String isback) {
        this.isback = isback;
    }
}
