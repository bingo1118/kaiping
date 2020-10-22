package com.smart.cloud.fire.activity.AssetManage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetByCkeyActivity;
import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetListActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.AssetInfo;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * 资产管理界面
 */
public class AssetManagerActivity extends MvpActivity<AssetPresenter> implements AssetView {

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    @Bind(R.id.all_cb)
    RadioButton all_cb;
    @Bind(R.id.completed_cb)
    RadioButton completed_cb;
    @Bind(R.id.no_completed_cb)
    RadioButton no_completed_cb;

    @Bind(R.id.assetlist)
    TextView assetlist;

    private AssetPresenter mPresenter;
    private AssetAdapter mAdapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_manager);
        ButterKnife.bind(this);
        mContext=this;
        mPresenter.getACheckList("");

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(all_cb.isChecked()){
                    mPresenter.getACheckList("");
                }else{
                    all_cb.setChecked(true);
                }
//                mPresenter.getACheckList("");
            }
        });
        all_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPresenter.getACheckList("");
                }
            }
        });
        completed_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPresenter.getACheckList("1");
                }
            }
        });
        no_completed_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPresenter.getACheckList("0");
                }
            }
        });
        assetlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AssetManagerActivity.this, AssetListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected AssetPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new AssetPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<ACheck> smokeList) {
        if(smokeList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new AssetAdapter(mContext, smokeList, mPresenter);
        mAdapter.setOnRecyclerViewItemClickListener(new AssetAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ACheck data) {
                Intent i=new Intent(AssetManagerActivity.this, AssetByCkeyActivity.class);
                i.putExtra("ckey",data.getCkey());
                startActivity(i);
            }
        });
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
        T.showShort(mContext,msg);
    }

    @Override
    public void onLoadingMore(List<AssetInfo> smokeList) {

    }
}
