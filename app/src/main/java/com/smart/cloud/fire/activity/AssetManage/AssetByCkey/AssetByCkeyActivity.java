package com.smart.cloud.fire.activity.AssetManage.AssetByCkey;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.activity.AssetManage.ACheck;
import com.smart.cloud.fire.activity.AssetManage.AssetAdapter;
import com.smart.cloud.fire.activity.AssetManage.AssetPresenter;
import com.smart.cloud.fire.activity.AssetManage.AssetView;
import com.smart.cloud.fire.activity.AssetManage.DealAsset.DealAssetActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.AssetInfo;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class AssetByCkeyActivity extends MvpActivity<AssetByCkeyPresenter> implements AssetByCkeyView {


    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    @Bind(R.id.change)
    TextView dealmore;
    @Bind(R.id.commit_btn)
    Button commit_btn;

    @Bind(R.id.all_cb)
    RadioButton all_cb;
    @Bind(R.id.completed_cb)
    RadioButton completed_cb;
    @Bind(R.id.no_completed_cb)
    RadioButton no_completed_cb;

    private AssetByCkeyPresenter mPresenter;
    private AssetByCkeyAdapter mAdapter;
    Context mContext;
    String ckey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_by_ckey);
        ButterKnife.bind(this);
        mContext=this;
        ckey=getIntent().getStringExtra("ckey");
        mPresenter.getACheckList(ckey,"","","","");

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(all_cb.isChecked()){
                    mPresenter.getACheckList(ckey,"","","","");
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
                    mPresenter.getACheckList(ckey,"","","","");
                }
            }
        });
        completed_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPresenter.getACheckList(ckey,"","1","","");
                }
            }
        });
        no_completed_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPresenter.getACheckList(ckey,"","0","","");
                }
            }
        });
    }

    private boolean isDealMore=false;
    @OnClick({R.id.change,R.id.commit_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.change:
                if(isDealMore){
                    dealmore.setText("批量处理");
                    mAdapter.setShowCheck(false);
                    commit_btn.setVisibility(View.GONE);
                    isDealMore=false;
                }else{
                    dealmore.setText("取消");
                    mAdapter.setShowCheck(true);
                    commit_btn.setVisibility(View.VISIBLE);
                    isDealMore=true;
                }
                break;
            case R.id.commit_btn:
                commit();
                dealmore.setText("批量处理");
                mAdapter.setShowCheck(false);
                commit_btn.setVisibility(View.GONE);
                isDealMore=false;
                break;
        }
    }

    private void commit() {
        VolleyHelper helper = VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url = ConstantValues.SERVER_IP_NEW + "checkAsset?ids=" + URLEncoder.encode(getListData())
                    + "&userId="+MyApp.getUserID()
                    + "&memo="
                    + "&picture="
                    + "&ckey="+ckey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode = response.getInt("errorcode");
                            String error = response.getString("error");
                            if (errorCode == 0) {
                                T.showShort(mContext, "记录上传成功");
                            } else {
                                T.showShort(mContext, error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext, "网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private String getListData() {
        String s="";
        List<String> list=mAdapter.getmList();
        for(String s1:list){
            s+=s1+",";
        }
        if(s.length()>1){
            s=s.substring(0,s.length()-1);
        }
        return s;
    }

    @Override
    protected AssetByCkeyPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new AssetByCkeyPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<AssetByCkeyEntity> smokeList) {
        if(smokeList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new AssetByCkeyAdapter(mContext, smokeList);
        recycler_view.setAdapter(mAdapter);
        swipereFreshLayout.setRefreshing(false);

        dealmore.setText("批量处理");
        mAdapter.setShowCheck(false);
        commit_btn.setVisibility(View.GONE);
        isDealMore=false;
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
    public void onLoadingMore(List<AssetByCkeyEntity> smokeList) {

    }
}
