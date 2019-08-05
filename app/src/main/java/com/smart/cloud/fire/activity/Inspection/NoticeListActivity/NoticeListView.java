package com.smart.cloud.fire.activity.Inspection.NoticeListActivity;

import java.util.List;

public interface NoticeListView {

    void getDataSuccess(List<NoticeEntity> pointList);
    void getDataFail(String msg);

}
