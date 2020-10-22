package com.smart.cloud.fire.activity.AssetManage;

import com.smart.cloud.fire.global.AssetInfo;

import java.util.List;

public interface AssetView {
    void getDataSuccess(List<ACheck> smokeList);
    void showLoading();
    void hideLoading();
    void getDataFail(String msg);
    void onLoadingMore(List<AssetInfo> smokeList);
}
