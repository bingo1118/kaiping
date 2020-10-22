package com.smart.cloud.fire.global;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssertObject;

public class AssetListEntity {
    private int errorCode;
    private String error;
    private AssertObject object;



    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }

    public AssertObject getObject() {
        return object;
    }

    public void setObject(AssertObject object) {
        this.object = object;
    }
}
