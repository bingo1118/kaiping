package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AssetManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetListActivity;
import com.smart.cloud.fire.activity.AssetManage.AssetManagerActivity;
import com.smart.cloud.fire.activity.AssetManage.Tag.TagListActivity;
import com.smart.cloud.fire.activity.AssetManage.TagAlarm.TagAlarmListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;


public class AssetHomeFragment extends Fragment {

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_asset_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.asset_list_rela,R.id.Label_list_rela,R.id.asset_task_list_rela,R.id.error_info_list_rela})
    public void onClick(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.asset_list_rela:
                intent=new Intent(mContext, AssetListActivity.class);
                break;
            case R.id.asset_task_list_rela:
                intent = new Intent(mContext, AssetManagerActivity.class);
                break;
            case R.id.error_info_list_rela:
                intent = new Intent(mContext, TagAlarmListActivity.class);
                break;
            case R.id.Label_list_rela:
                intent = new Intent(mContext, TagListActivity.class);
                break;
        }
        startActivity(intent);
    }


}
