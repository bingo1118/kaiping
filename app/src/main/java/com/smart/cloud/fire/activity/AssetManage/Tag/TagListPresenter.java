package com.smart.cloud.fire.activity.AssetManage.Tag;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class TagListPresenter extends BasePresenter<TagListView> {

    public TagListPresenter(TagListView view) {
        attachView(view);
    }

    public void getTagList(String userId,String privilege, String areaId,String mac,String name,
                           String netstate, String page) {
        Observable mObservable;
        mObservable = apiStores1.getTagList(  userId, privilege,  areaId, mac, name,
                 netstate,  page);
        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<TagListEntity>() {
            @Override
            public void onSuccess(TagListEntity model) {
                int result = model.getErrorCode();
                if (result == 0) {
                    List<Smoke> smokeList = model.getList();
                    if(smokeList==null){
                        List<Smoke> mSmokeList = new ArrayList<>();
                        mvpView.getDataSuccess(mSmokeList);
                        mvpView.getDataFail("无数据");
                    }else{
                        mvpView.getDataSuccess(smokeList);
                    }
                } else {
                    List<Smoke> mSmokeList = new ArrayList<>();
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

