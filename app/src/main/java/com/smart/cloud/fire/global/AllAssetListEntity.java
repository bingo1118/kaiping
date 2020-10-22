package com.smart.cloud.fire.global;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetByCkeyEntity;

import java.util.List;

public class AllAssetListEntity {

    private int errorCode;
    private String error;
    private List<AssetByCkeyEntity> list;
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

    public List<AssetByCkeyEntity> getList() {
        return list;
    }

    public void setList(List<AssetByCkeyEntity> list) {
        this.list = list;
    }
}
