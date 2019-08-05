package com.smart.cloud.fire.activity.Inspection.NoticeListActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.base.ui.MvpV4Fragment;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class NoticeFragment extends MvpFragment<NoticeListPresenter> implements NoticeListView{

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.null_data_iv)
    TextView null_data_iv;

    Context mContext;
    NoticeListPresenter mPresenter;
    NoticeListAdapter mAdapter;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notice_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext=getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        mPresenter.getNoticeItems(userID);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNoticeItems(userID);
            }
        });
    }

    @Override
    public String getFragmentName() {
        return "NOTICE";
    }

    @Override
    protected NoticeListPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new NoticeListPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<NoticeEntity> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
            null_data_iv.setVisibility(View.VISIBLE);
        }else{
            null_data_iv.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new NoticeListAdapter(mContext, pointList);
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
        null_data_iv.setVisibility(View.VISIBLE);
        swipereFreshLayout.setRefreshing(false);
    }
}
