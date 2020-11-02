package com.smart.cloud.fire.order.OrderNotice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetListActivity;
import com.smart.cloud.fire.mvp.Inspction.InspMainActivity;
import com.smart.cloud.fire.order.OrderList.OrderListActivity;

import fire.cloud.smart.com.smartcloudfire.R;

public class OrderNoticeActivity extends AppCompatActivity {

    TextView tv;
    TextView title_tv,content_tv;
    String title;
    String notice;
    String memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_notice);

        title=getIntent().getStringExtra("title");
        notice=getIntent().getStringExtra("notice");
        memo=getIntent().getStringExtra("content");
        title_tv=(TextView)findViewById(R.id.title_tv) ;
        content_tv=(TextView)findViewById(R.id.content_tv) ;
        title_tv.setText(title);
        content_tv.setText(memo);
        tv=(TextView)findViewById(R.id.commit);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(notice!=null&&notice.equals("1")){
                    i=new Intent(OrderNoticeActivity.this, OrderListActivity.class);
                }else{
                    i=new Intent(OrderNoticeActivity.this, InspMainActivity.class);
                }
                startActivity(i);
                finish();
            }
        });
    }
}
