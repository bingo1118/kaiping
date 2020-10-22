package com.smart.cloud.fire.order.OrderNotice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetListActivity;
import com.smart.cloud.fire.order.OrderList.OrderListActivity;

import fire.cloud.smart.com.smartcloudfire.R;

public class OrderNoticeActivity extends AppCompatActivity {

    TextView tv;
    TextView title_tv;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_notice);

        title=getIntent().getStringExtra("title");
        title_tv=(TextView)findViewById(R.id.title_tv) ;
        title_tv.setText(title);
        tv=(TextView)findViewById(R.id.commit) ;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OrderNoticeActivity.this, OrderListActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
