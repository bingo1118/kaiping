package com.smart.cloud.fire.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smart.cloud.fire.activity.Inspection.NoticeListActivity.NoticeListActivity;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListActivity;

import fire.cloud.smart.com.smartcloudfire.R;

public class NoticeDialogActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_dialog);

        tv=(TextView)findViewById(R.id.commit) ;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NoticeDialogActivity.this, NoticeListActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
