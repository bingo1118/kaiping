package com.smart.cloud.fire.activity.Inspection.NoticeListActivity;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import rx.Observable;

public class NoticeListPresenter extends BasePresenter<NoticeListView> {

    public NoticeListPresenter(NoticeListView view) {
        super();
        attachView(view);
    }

    public void getNoticeItems(String userid){
        Observable mObservable=apiStores1.getNoticeByUserId(userid);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                if(model.getErrorCode()==0){
                    mvpView.getDataSuccess(model.getListNotice());
                }else{
                    mvpView.getDataFail(model.getError());
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误");
            }
            @Override
            public void onCompleted() {
            }
        }));
    }

}
