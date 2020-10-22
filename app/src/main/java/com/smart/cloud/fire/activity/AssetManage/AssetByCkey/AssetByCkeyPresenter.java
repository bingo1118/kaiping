package com.smart.cloud.fire.activity.AssetManage.AssetByCkey;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.AllAssetListEntity;
import com.smart.cloud.fire.global.AssetListEntity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class AssetByCkeyPresenter extends BasePresenter<AssetByCkeyView> {

    public AssetByCkeyPresenter(AssetByCkeyView view) {
        attachView(view);
    }

    public void getACheckList(String ckey,String atPid,String ifFinish, String startTime,String endTime) {
        Observable mObservable;
        mObservable = apiStores1.getAssetByCkey( ckey, atPid, ifFinish,  startTime, endTime);
        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<AssetListEntity>() {
            @Override
            public void onSuccess(AssetListEntity model) {
                int result = model.getErrorCode();
                if (result == 0) {
                    List<AssetByCkeyEntity> smokeList = model.getObject().getList();
                    if(smokeList==null){
                        List<AssetByCkeyEntity> mSmokeList = new ArrayList<>();
                        mvpView.getDataSuccess(mSmokeList);
                        mvpView.getDataFail("无数据");
                    }else{
                        mvpView.getDataSuccess(smokeList);
                    }
                } else {
                    List<AssetByCkeyEntity> mSmokeList = new ArrayList<>();
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


    public void getAllAssetListList(String userId,String privilege,int page,String akey,
                                    String areaId, String atId, String named, String state) {
        Observable mObservable;
        mObservable = apiStores1.getAssetList( userId, privilege,page+"",akey,areaId,atId,named,state);
        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<AllAssetListEntity>() {
            @Override
            public void onSuccess(AllAssetListEntity model) {
                int result = model.getErrorCode();
                if (result == 0) {
                    List<AssetByCkeyEntity> smokeList = model.getList();
                    if(smokeList==null){
                        List<AssetByCkeyEntity> mSmokeList = new ArrayList<>();
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
                    List<AssetByCkeyEntity> mSmokeList = new ArrayList<>();
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
