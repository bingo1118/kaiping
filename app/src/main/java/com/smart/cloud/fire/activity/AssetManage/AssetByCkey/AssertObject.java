package com.smart.cloud.fire.activity.AssetManage.AssetByCkey;

import com.smart.cloud.fire.activity.AssetManage.AcNumber;

import java.util.List;

public class AssertObject {
    private AcNumber acNumber;
    private List<AssetByCkeyEntity> list;

    public AcNumber getAcNumber() {
        return acNumber;
    }

    public void setAcNumber(AcNumber acNumber) {
        this.acNumber = acNumber;
    }


    public List<AssetByCkeyEntity> getList() {
        return list;
    }

    public void setList(List<AssetByCkeyEntity> list) {
        this.list = list;
    }
}
