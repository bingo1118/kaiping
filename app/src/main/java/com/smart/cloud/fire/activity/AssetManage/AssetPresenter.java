package com.smart.cloud.fire.activity.AssetManage;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.AssetInfo;
import com.smart.cloud.fire.global.AssetManager;
import com.smart.cloud.fire.global.CheckListEntity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class AssetPresenter extends BasePresenter<AssetView> {

    public AssetPresenter(AssetView view) {
        attachView(view);
    }

    public void getACheckList(String state) {
        Observable mObservable;
        mObservable = apiStores1.getACheckList(MyApp.getUserID(), MyApp.getPrivilege()+"", state);
        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<CheckListEntity>() {
            @Override
            public void onSuccess(CheckListEntity model) {
                int result = model.getErrorCode();
                if (result == 0) {
                    List<ACheck> smokeList = model.getObject().getList();
                    if(smokeList==null){
                        List<ACheck> mSmokeList = new ArrayList<>();
                        mvpView.getDataSuccess(mSmokeList);
                        mvpView.getDataFail("无数据");
                    }else{
                        mvpView.getDataSuccess(smokeList);
                    }
                } else {
                    List<ACheck> mSmokeList = new ArrayList<>();
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
