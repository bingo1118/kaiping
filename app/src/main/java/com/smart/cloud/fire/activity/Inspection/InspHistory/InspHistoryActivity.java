package com.smart.cloud.fire.activity.Inspection.InspHistory;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.Inspection.ItemsList.ItemsListAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspHistoryActivity extends MvpActivity<InspHistoryPresenter> implements InspHistoryView{

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    String uid;

    private InspHistoryPresenter mPresenter;
    private InspHistoryAdapter mAdapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insp_history);

        ButterKnife.bind(this);
        mContext=this;
        uid=getIntent().getStringExtra("uid");
        mPresenter.getRecordList(uid);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRecordList(uid);
            }
        });
    }

    @Override
    protected InspHistoryPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new InspHistoryPresenter(this);
        }
        return mPresenter;
    }


    @Override
    public void getDataSuccess(List<InspHistoryEntity> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new InspHistoryAdapter(mContext, pointList);
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);

    }


    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
    }

}
