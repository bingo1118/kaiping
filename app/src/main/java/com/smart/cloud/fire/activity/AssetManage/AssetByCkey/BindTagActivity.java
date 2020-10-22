package com.smart.cloud.fire.activity.AssetManage.AssetByCkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.blankj.utilcode.util.StringUtils;
import com.obsessive.zbar.CaptureActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class BindTagActivity extends Activity {

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;
    @Bind(R.id.uid_edit)
    EditText uid_edit;
    @Bind(R.id.repeatr_edit)
    EditText repeatr_edit;

    private Context mContext;
    AssetByCkeyEntity mPoint;
    private int isSuccees=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_tag);

        ButterKnife.bind(this);
        mPoint = (AssetByCkeyEntity) getIntent().getSerializableExtra("info");
        mContext = this;
        initView();
    }

    private void initView() {
        addFireDevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBarOnUiThread();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(StringUtils.isEmpty(uid_edit.getText().toString())){
                            T.showShort(mContext,"请录入标签编码");
                        }
                        if(StringUtils.isEmpty(repeatr_edit.getText().toString())){
                            T.showShort(mContext,"请录入主机号");
                        }

                        VolleyHelper helper = VolleyHelper.getInstance(mContext);
                        RequestQueue mQueue = helper.getRequestQueue();
                        String url = ConstantValues.SERVER_IP_NEW + "bindTag?mac=" + uid_edit.getText().toString()
                                    + "&repeater="+repeatr_edit.getText().toString()
                                    + "&akey="+mPoint.getAkey()
                                    + "&bind=true";


                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            int errorCode = response.getInt("errorcode");
                                            String error = response.getString("error");
                                            if (errorCode == 0) {
                                                T.showShort(mContext, "绑定成功");
                                                isSuccees=1;
                                                finish();
                                            } else {
                                                T.showShort(mContext, error);
                                            }
                                            dismissProgressBarOnUiThread();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            dismissProgressBarOnUiThread();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                T.showShort(mContext, "网络错误");
                                dismissProgressBarOnUiThread();
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }
                }).start();

            }
        });
    }


    @Override
    protected void onDestroy() {
        setResult(isSuccees);
        super.onDestroy();
    }

    @OnClick({R.id.scan_er_wei_ma})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_er_wei_ma:
                Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    uid_edit.setText(scanResult);

                }
                break;
        }

    }


    private void showProgressBarOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void dismissProgressBarOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

}
