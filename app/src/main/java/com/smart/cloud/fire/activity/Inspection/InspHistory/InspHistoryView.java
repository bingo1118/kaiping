package com.smart.cloud.fire.activity.Inspection.InspHistory;

import java.util.List;

public interface InspHistoryView {

    void getDataSuccess(List<InspHistoryEntity> pointList);
    void getDataFail(String msg);
}
