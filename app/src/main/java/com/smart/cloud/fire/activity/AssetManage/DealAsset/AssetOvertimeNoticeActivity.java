package com.smart.cloud.fire.activity.AssetManage.DealAsset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smart.cloud.fire.activity.AssetManage.AssetManagerActivity;
import com.smart.cloud.fire.order.OrderList.OrderListActivity;
import com.smart.cloud.fire.order.OrderNotice.OrderNoticeActivity;

import fire.cloud.smart.com.smartcloudfire.R;

public class AssetOvertimeNoticeActivity extends AppCompatActivity {

    TextView tv;
    TextView title_tv;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_overtime_notice);

        title=getIntent().getStringExtra("title");
        title_tv=(TextView)findViewById(R.id.title_tv) ;
        title_tv.setText(title);
        tv=(TextView)findViewById(R.id.commit) ;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AssetOvertimeNoticeActivity.this, AssetManagerActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}

