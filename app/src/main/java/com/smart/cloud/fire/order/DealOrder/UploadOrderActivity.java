package com.smart.cloud.fire.order.DealOrder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class UploadOrderActivity extends Activity {

    @Bind(R.id.spinner)
    Spinner spinner;

    Context mContext;
    JobOrder order;
    ArrayList<DealerBean> dealerBeans;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_order);

        mContext = this;
        order = (JobOrder) getIntent().getSerializableExtra("order");
        ButterKnife.bind(this);
        getDealerList();
    }

    @OnClick({R.id.commit_btn})
    public void onClick(View v) {
        commit();
    }

    private void commit() {
        VolleyHelper helper = VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url = ConstantValues.SERVER_IP_NEW + "uploadOrder?jkey=" + order.getJkey()
                + "&principal=" + userid
                + "&loginUserId=" + MyApp.getUserID();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            T.showShort(mContext, response.getString("error"));
                        } catch (Exception e) {
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


    private void getDealerList() {
        VolleyHelper helper = VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url = ConstantValues.SERVER_IP_NEW + "getPAreaUser?areaId=" + order.getAreaId();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode = response.getInt("errorcode");
                            String error = response.getString("error");
                            if (errorCode == 0) {
                                dealerBeans = new ArrayList<>();
                                JSONArray jsonArray = response.getJSONArray("list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DealerBean bean = new DealerBean(jsonArray.getJSONObject(i).getString("userId")
                                            , jsonArray.getJSONObject(i).getString("named"));
                                    dealerBeans.add(bean);
                                }
                                DealerAdapter adapter = new DealerAdapter(mContext, dealerBeans);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                        userid = ((DealerBean) adapter.getItem(pos)).getUserId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                T.showShort(mContext, error);
                            }
                        } catch (Exception e) {
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
}


