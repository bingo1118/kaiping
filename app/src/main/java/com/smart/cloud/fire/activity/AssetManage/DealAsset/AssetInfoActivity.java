package com.smart.cloud.fire.activity.AssetManage.DealAsset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.AssetByCkeyEntity;
import com.smart.cloud.fire.activity.AssetManage.AssetByCkey.BindTagActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class AssetInfoActivity extends Activity {

    @Bind(R.id.asset_name_tv)
    TextView asset_name_tv;
    @Bind(R.id.asset_id_tv)
    TextView asset_id_tv;
    @Bind(R.id.manager_tv)
    TextView manager_tv;
    @Bind(R.id.phone_tv)
    TextView phone_tv;
    @Bind(R.id.state_tv)
    TextView state_tv;
    @Bind(R.id.area_tv)
    TextView area_tv;
    @Bind(R.id.type_tv)
    TextView type_tv;
    @Bind(R.id.add_time_tv)
    TextView add_time_tv;
    @Bind(R.id.overtime_tv)
    TextView overtime_tv;
    @Bind(R.id.location_tv)
    TextView location_tv;
    @Bind(R.id.bind_tag_switch)
    Switch bind_tag_switch;

    AssetByCkeyEntity mPoint;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_info);

        mContext = this;
        ButterKnife.bind(this);
        mPoint = (AssetByCkeyEntity) getIntent().getSerializableExtra("info");
        if (mPoint != null) {
            initView();
        }
    }

    private void initView() {
        asset_name_tv.setText(mPoint.getNamed());
        asset_id_tv.setText(mPoint.getAkey());
        manager_tv.setText(mPoint.getPrincipal());
        phone_tv.setText(mPoint.getPhone());
        state_tv.setText(mPoint.getStateName());
        area_tv.setText(mPoint.getAreaName());
        type_tv.setText(mPoint.getAtName());
        add_time_tv.setText(mPoint.getAddTime());
        overtime_tv.setText(mPoint.getOverTime());
        location_tv.setText(mPoint.getAddress());

        bind_tag_switch.setEnabled(true);
        bind_tag_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent i = new Intent(mContext, BindTagActivity.class);
                    i.putExtra("info", mPoint);
                    startActivityForResult(i, 100);
                } else {
                    cancelBindTag(mPoint.getAkey());
                }
            }
        });
        if(mPoint.getMac().length()>3){
            bind_tag_switch.setSelected(true);
        }else{
            bind_tag_switch.setSelected(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            bind_tag_switch.setSelected(resultCode==1?true:false);
        }
    }

    private void cancelBindTag(String akey) {
        VolleyHelper helper = VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url = ConstantValues.SERVER_IP_NEW + "bindTag?akey="+mPoint.getAkey()
                + "&bind=false";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode = response.getInt("errorcode");
                            String error = response.getString("error");
                            if (errorCode == 0) {
                                T.showShort(mContext, "解绑成功");
                                bind_tag_switch.setSelected(false);
                            } else {
                                T.showShort(mContext, error);
                                bind_tag_switch.setSelected(true);
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

    @OnClick({R.id.update_tv, R.id.upload_tv})
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.update_tv:
                i = new Intent(mContext, UpdateAssetDataActivity.class);
                i.putExtra("order", mPoint);
                startActivity(i);
                break;
            case R.id.upload_tv:
                i = new Intent(mContext, UploadAssetProblemActivity.class);
                i.putExtra("akey", mPoint.getAkey());
                i.putExtra("areaId", mPoint.getAreaId());
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
