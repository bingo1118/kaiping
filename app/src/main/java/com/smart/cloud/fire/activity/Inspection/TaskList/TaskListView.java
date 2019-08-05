package com.smart.cloud.fire.activity.Inspection.TaskList;

import com.smart.cloud.fire.global.InspectionTask;

import java.util.List;

public interface TaskListView {
    void getDataSuccess(List<InspectionTask> pointList);

    void getSumDataSuccess(int todoNum ,int progreaNum,int complishNum);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();
}
