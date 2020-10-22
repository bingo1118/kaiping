package com.smart.cloud.fire.activity.AssetManage.TagAlarm;

import java.io.Serializable;

public class TagAlarmInfo implements Serializable{
    private int type=1;

    private String area;
    private String areaId;
    private String mac;
    private String repeaterMac;
    private String akey;
    private String named;
    private String address;
    private String alarmType;
    private String alarmName;
    private String alarmTime;
    private String tagName;

    private String princapal;
    private String phone;
    private String photo;

    private String parentId;

    private int ifDeal;//是否处理状态，0未处理1已处理
    private String ifDealName;//是否处理名称
    private String dealUser;//处理人
    private String dealTime;//处理时间
    private String dealDesc;//处理情况



    public String getIfDealName() {
        return ifDealName;
    }

    public void setIfDealName(String ifDealName) {
        this.ifDealName = ifDealName;
    }

    public String getDealUser() {
        return dealUser;
    }

    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }

    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getAreaId() {
        return areaId;
    }
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getRepeaterMac() {
        return repeaterMac;
    }
    public void setRepeaterMac(String repeaterMac) {
        this.repeaterMac = repeaterMac;
    }
    public String getAkey() {
        return akey;
    }
    public void setAkey(String akey) {
        this.akey = akey;
    }
    public String getNamed() {
        return named;
    }
    public void setNamed(String named) {
        this.named = named;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAlarmType() {
        return alarmType;
    }
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
    public String getAlarmName() {
        return alarmName;
    }
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }
    public String getAlarmTime() {
        return alarmTime;
    }
    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    public String getPrincapal() {
        return princapal;
    }
    public void setPrincapal(String princapal) {
        this.princapal = princapal;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getIfDeal() {
        return ifDeal;
    }

    public void setIfDeal(int ifDeal) {
        this.ifDeal = ifDeal;
    }
}
