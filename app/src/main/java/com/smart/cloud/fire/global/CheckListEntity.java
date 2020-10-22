package com.smart.cloud.fire.global;

import com.smart.cloud.fire.activity.AssetManage.ACheck;
import com.smart.cloud.fire.activity.AssetManage.ACheckListObject;

import java.util.List;

public class CheckListEntity {

    private int errorCode;
    private String error;
    private ACheckListObject object;



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


    public ACheckListObject getObject() {
        return object;
    }

    public void setObject(ACheckListObject object) {
        this.object = object;
    }
}
