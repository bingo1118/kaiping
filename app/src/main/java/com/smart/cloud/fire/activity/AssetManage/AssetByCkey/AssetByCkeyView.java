package com.smart.cloud.fire.activity.AssetManage.AssetByCkey;

import java.util.List;

public interface AssetByCkeyView {
    void getDataSuccess(List<AssetByCkeyEntity> smokeList);
    void showLoading();
    void hideLoading();
    void getDataFail(String msg);
    void onLoadingMore(List<AssetByCkeyEntity> smokeList);
}
