package com.smart.cloud.fire.activity.Inspection.NoticeListActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class NoticeDetailActivity extends Activity {

    @Bind(R.id.grade_tv)
    TextView grade_tv;
    @Bind(R.id.time_tv)
    TextView time_tv;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.content_tv)
    TextView content_tv;
    @Bind(R.id.publicer_tv)
    TextView publicer_tv;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        ButterKnife.bind(this);
        mContext=this;

        NoticeEntity noticeEntity= (NoticeEntity) getIntent().getSerializableExtra("notice");
        grade_tv.setText(noticeEntity.getNlevelName());
        time_tv.setText(noticeEntity.getPublishtime());
        title_tv.setText(noticeEntity.getTitle());
        content_tv.setText(noticeEntity.getContent());
        publicer_tv.setText("发布者:"+noticeEntity.getContent());

        if(noticeEntity.getIsback().equals("0")){
            reply(noticeEntity.getId());//消息已读回复
        }
    }

    private void reply(long id) {
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url="";

        url= ConstantValues.SERVER_IP_NEW+"noticeBack?userId="+ MyApp.getUserID()+"&id="+id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("errorCode")==0){
                                T.showShort(mContext,"消息已读");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext,"网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }
}
