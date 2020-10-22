package com.smart.cloud.fire.activity.AssetManage.AssetByCkey;

import java.io.Serializable;

public class AssetByCkeyEntity implements Serializable{

    private String id;
    private String akey;
    private String areaId;
    private String atPid;
    private String atId;
    private String named;
    private String memo;
    private int state;
    private String address;
    private String principal;
    private String phone;
    private String addTime;
    private String overTime;

    private String area;
    private String pnamed;
    private String apnamed;
    private String stateName;

    private String ckey;
    private int ifFinish;
    private String ifFinishName;
    private String startTime;
    private String endTime;

    private String atName;
    private String areaName;

    private String checkUser;
    private String checkTime;

    private String mac;//绑定的底座
    private String photo;

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAkey() {
        return akey;
    }
    public void setAkey(String akey) {
        this.akey = akey;
    }
    public String getAreaId() {
        return areaId;
    }
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    public String getAtPid() {
        return atPid;
    }
    public void setAtPid(String atPid) {
        this.atPid = atPid;
    }
    public String getAtId() {
        return atId;
    }
    public void setAtId(String atId) {
        this.atId = atId;
    }
    public String getNamed() {
        return named;
    }
    public void setNamed(String named) {
        this.named = named;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPrincipal() {
        return principal;
    }
    public void setPrincipal(String principal) {
        this.principal = principal;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getOverTime() {
        return overTime;
    }
    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getPnamed() {
        return pnamed;
    }
    public void setPnamed(String pnamed) {
        this.pnamed = pnamed;
    }
    public String getApnamed() {
        return apnamed;
    }
    public void setApnamed(String apnamed) {
        this.apnamed = apnamed;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public String getCkey() {
        return ckey;
    }
    public void setCkey(String ckey) {
        this.ckey = ckey;
    }
    public int getIfFinish() {
        return ifFinish;
    }
    public void setIfFinish(int ifFinish) {
        this.ifFinish = ifFinish;
    }
    public String getIfFinishName() {
        return ifFinishName;
    }
    public void setIfFinishName(String ifFinishName) {
        this.ifFinishName = ifFinishName;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAtName() {
        return atName;
    }

    public void setAtName(String atName) {
        this.atName = atName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
