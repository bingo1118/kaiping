package com.smart.cloud.fire.order.OrderList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadProblemActivity;
import com.smart.cloud.fire.activity.Inspection.UploadMsg.UploadMsgActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class OrderListActivity extends MvpActivity<OrderListPresenter> implements OrderListEntity {

    Context mContext;
    private OrderListPresenter mPresenter;
    private LinearLayoutManager linearLayoutManager;
    private List<JobOrder> list;
    private OrderListAdapter mAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.change)
    TextView change;
    @Bind(R.id.problem)
    TextView problem;
//    @Bind(R.id.mProgressBar)
//    ProgressBar mProgressBar;

    int state = 0;
    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        mContext = this;
        refreshListView();
        userid = SharedPreferencesManager.getInstance().getData(MyApp.app,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);

        mPresenter.getAllDev(userid, state);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadProblemActivity.class);
                startActivity(intent);
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
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //下拉刷新。。
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state = 0;
                mPresenter.getAllDev(userid, state);
            }
        });
    }

    @Override
    protected OrderListPresenter createPresenter() {
        mPresenter = new OrderListPresenter(this);
        return mPresenter;
    }


    @Override
    public void getDataSuccess(List<JobOrder> smokeList) {
        list = new ArrayList<>();
        list.addAll(smokeList);
        mAdapter = new OrderListAdapter(mContext, list);
        recyclerView.setAdapter(mAdapter);
        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {

//        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

//        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void unSubscribe(String type) {

    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
    }

    private PopupWindow popupWindow = null;
    int state_temp = 0;

    /**
     * 打开下拉列表弹窗
     */
    public void showPopWindow() {
        // 加载popupWindow的布局文件

        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) mContext.getSystemService(infServie);
        View contentView = layoutInflater.inflate(R.layout.choose_order_condition, null, false);

        RadioGroup radioGroup_status = (RadioGroup) contentView.findViewById(R.id.status_rg);
        radioGroup_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.status1_rb:
                        state_temp = 0;
                        break;
                    case R.id.status2_rb:
                        state_temp = 1;
                        break;
                    case R.id.status3_rb:
                        state_temp = 2;
                        break;
                    case R.id.status4_rb:
                        state_temp = 6;
                        break;
                    case R.id.status5_rb:
                        state_temp = 3;
                        break;
                    case R.id.status6_rb:
                        state_temp = 5;
                        break;
                    case R.id.status7_rb:
                        state_temp = 4;
                        break;
                }
            }
        });


        Button commit = (Button) contentView.findViewById(R.id.commit_btn);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = state_temp;
                state_temp = 0;
                mPresenter.getAllDev(userid, state);
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_item_color_bg));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
            }
        });//@@12.20
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
//        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                backgroundAlpha(1f);
                popupWindow = null;
            }
        });
        popupWindow.showAsDropDown(change);
    }
}
