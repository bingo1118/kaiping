package com.smart.cloud.fire.activity.AssetManage.TagAlarm;

import com.smart.cloud.fire.global.TagAlarmListEntity;

import java.util.List;

public interface TagAlarmListView {
    void getDataSuccess(List<TagAlarmInfo> smokeList);
    void showLoading();
    void hideLoading();
    void getDataFail(String msg);
    void onLoadingMore(List<TagAlarmInfo> smokeList);
}
