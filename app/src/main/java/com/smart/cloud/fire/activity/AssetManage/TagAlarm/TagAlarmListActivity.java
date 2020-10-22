package com.smart.cloud.fire.activity.AssetManage.TagAlarm;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.TagAlarmListEntity;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TagAlarmListActivity extends MvpActivity<TagAlarmListPresenter> implements TagAlarmListView {

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    @Bind(R.id.rb0)
    RadioButton all_cb;
    @Bind(R.id.rb1)
    RadioButton completed_cb;
    @Bind(R.id.rb2)
    RadioButton no_completed_cb;


    private TagAlarmListPresenter mPresenter;
    private TagAlarmListAdapter mAdapter;
    Context mContext;
    int page = 1;
    List<TagAlarmInfo> mList;

    private int loadMoreCount;
    private int lastVisibleItem;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_alarm_list);
        ButterKnife.bind(this);
        mContext = this;

        mPresenter.getTabAlarmList(MyApp.getUserID(), MyApp.getPrivilege() + "", page,"");
        refreshListView();
        all_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    page=1;
                    mPresenter.getTabAlarmList(MyApp.getUserID(), MyApp.getPrivilege() + "", page,"");
                }
            }
        });
        completed_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    page=1;
                    mPresenter.getTabAlarmList(MyApp.getUserID(), MyApp.getPrivilege() + "", page,"1");
                }
            }
        });
        no_completed_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    page=1;
                    mPresenter.getTabAlarmList(MyApp.getUserID(), MyApp.getPrivilege() + "", page,"0");
                }
            }
        });
    }

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipereFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipereFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipereFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        //下拉刷新。。
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                all_cb.setChecked(true);
            }
        });


        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mAdapter == null) {
                    return;
                }
                int count = mAdapter.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == count) {
                    if (loadMoreCount >= 10) {
                        page += 1;
                        if(all_cb.isChecked()){
                            mPresenter.getTabAlarmList(MyApp.getUserID(), MyApp.getPrivilege() + "", page,"");
                        }else if(completed_cb.isChecked()){
                            mPresenter.getTabAlarmList(MyApp.getUserID(), MyApp.getPrivilege() + "", page,"1");
                        }else{
                            mPresenter.getTabAlarmList(MyApp.getUserID(), MyApp.getPrivilege() + "", page,"0");
                        }
                    } else {
                        T.showShort(mContext, "已经没有更多数据了");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected TagAlarmListPresenter createPresenter() {
        if (mPresenter == null) {
            mPresenter = new TagAlarmListPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<TagAlarmInfo> smokeList) {
        if (smokeList.size() == 0) {
            Toast.makeText(mContext, "无数据", Toast.LENGTH_SHORT).show();
        }//@@7.7
        mList=smokeList;
        loadMoreCount = smokeList.size();
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new TagAlarmListAdapter(mContext, mList);
        recycler_view.setAdapter(mAdapter);
        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
    }

    @Override
    public void onLoadingMore(List<TagAlarmInfo> smokeList) {
        loadMoreCount = smokeList.size();
        mList.addAll((List<TagAlarmInfo>)smokeList);
        mAdapter.notifyDataSetChanged();
    }


}