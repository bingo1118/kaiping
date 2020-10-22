package com.smart.cloud.fire.activity.AssetManage.Tag;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.List;

public class TagListEntity {

    private int errorCode;
    private String error;
    private List<Smoke> list;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Smoke> getList() {
        return list;
    }

    public void setList(List<Smoke> list) {
        this.list = list;
    }
}
