package com.smart.cloud.fire.activity.Inspection.NoticeListActivity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class NoticeListActivity extends MvpActivity<NoticeListPresenter> implements NoticeListView{

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        ButterKnife.bind(this);

        mContext=this;
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
        null_data_iv.setVisibility(View.GONE);
        swipereFreshLayout.setRefreshing(false);
    }
}
