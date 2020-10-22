package com.smart.cloud.fire.activity.AssetManage.TagAlarm;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.TagAlarmListEntity;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class TagAlarmListPresenter extends BasePresenter<TagAlarmListView> {

    public TagAlarmListPresenter(TagAlarmListView view) {
        attachView(view);
    }




    public void getTabAlarmList(String userId,String privilege,int page,String ifDeal) {
        Observable mObservable;
        mObservable = apiStores1.getTabAlarmList( userId, privilege,page+"",ifDeal);
        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<TagAlarmListEntity>() {
            @Override
            public void onSuccess(TagAlarmListEntity model) {
                int result = model.getErrorcode();
                if (result == 0) {
                    List<TagAlarmInfo> smokeList = model.getList();
                    if(smokeList==null){
                        List<TagAlarmInfo> mSmokeList = new ArrayList<>();
                        if(page>1){
                            mvpView.onLoadingMore(mSmokeList);
                        }else{
                            mvpView.getDataSuccess(mSmokeList);
                            mvpView.getDataFail("无数据");
                        }
                    }else{
                        if(page>1){
                            mvpView.onLoadingMore(smokeList);
                        }else{
                            mvpView.getDataSuccess(smokeList);
                        }
                    }
                } else {
                    List<TagAlarmInfo> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList);
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {

                mvpView.getDataFail("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
