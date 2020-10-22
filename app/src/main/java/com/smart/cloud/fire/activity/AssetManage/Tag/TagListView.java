package com.smart.cloud.fire.activity.AssetManage.Tag;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.List;

public interface TagListView {
    void getDataSuccess(List<Smoke> smokeList);
    void showLoading();
    void hideLoading();
    void getDataFail(String msg);
    void onLoadingMore(List<Smoke> smokeList);
}
