package com.smart.cloud.fire.activity.AssetManage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetListActivity;
import com.smart.cloud.fire.activity.AssetManage.Tag.TagListActivity;
import com.smart.cloud.fire.activity.AssetManage.TagAlarm.TagAlarmListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class AssetHomeActivity extends Activity {

    Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_home);

        ButterKnife.bind(this);
        mContext=this;
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
