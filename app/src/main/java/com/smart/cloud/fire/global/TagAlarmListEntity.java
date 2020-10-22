package com.smart.cloud.fire.global;

import com.smart.cloud.fire.activity.AssetManage.TagAlarm.TagAlarmInfo;

import java.util.List;

public class TagAlarmListEntity {

    private int errorcode;
    private String error;
    private List<TagAlarmInfo> list;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<TagAlarmInfo> getList() {
        return list;
    }

    public void setList(List<TagAlarmInfo> list) {
        this.list = list;
    }
}
