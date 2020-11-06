package com.smart.cloud.fire.activity.Inspection.InspectionMain;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.activity.AddDev.ChioceDevTypeActivity;
import com.smart.cloud.fire.activity.AssetManage.AssetHomeActivity;
import com.smart.cloud.fire.activity.AssetManage.AssetManagerActivity;
import com.smart.cloud.fire.activity.Camera.CameraDevActivity;
import com.smart.cloud.fire.activity.Host.HostActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNFCItemActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNormalItemActivity;
import com.smart.cloud.fire.activity.Inspection.InspectionMap.InspectionMapActivity;
import com.smart.cloud.fire.activity.Inspection.NoticeListActivity.NoticeListActivity;
import com.smart.cloud.fire.activity.Inspection.PointList.PointListActivity;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadInspectionInfoActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadProblemActivity;
import com.smart.cloud.fire.activity.Inspection.UploadMsg.UploadMsgActivity;
import com.smart.cloud.fire.activity.Setting.MyZoomActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.main.Main3Activity;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspSettingFragment extends Fragment {

    @Bind(R.id.notice_num_tv)
    TextView notice_num_tv;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_insp_setting, container, false);
        ButterKnife.bind(this, view);
        mContext=getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNoticeCount();
    }

    private void getNoticeCount() {
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url="";

        url= ConstantValues.SERVER_IP_NEW+"getNoticeByUserId?userId="+ MyApp.getUserID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int nobackCount=response.getInt("nobackCount");
                            if(nobackCount==0){
                                notice_num_tv.setVisibility(View.GONE);
                                notice_num_tv.setText(nobackCount+"");
                            }else{
                                notice_num_tv.setVisibility(View.VISIBLE);
                                notice_num_tv.setText(nobackCount+"");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.insp_add_normal_ib,R.id.insp_add_nfc_ib
            ,R.id.insp_points_ib,R.id.insp_upload_problem_ib
            ,R.id.my_image,R.id.insp_notice_ib
            ,R.id.insp_add_fire_ib,R.id.insp_upload_msg_ib,R.id.host_info_ib,R.id.camera_ib})
    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.my_image:
                intent = new Intent(mContext, MyZoomActivity.class);
                break;
            case R.id.insp_add_normal_ib:
                if(MyApp.getPrivilege()==11){
                    T.showShort(mContext,"该账号不具备该权限");
                }else{
                    intent=new Intent(mContext, AddInspectionNormalItemActivity.class);
                }
                break;
            case R.id.insp_add_nfc_ib:
                if(MyApp.getPrivilege()==11){
                    T.showShort(mContext,"该账号不具备该权限");
                }else{
                    intent=new Intent(mContext, AddInspectionNFCItemActivity.class);
                }
                break;
            case R.id.insp_points_ib:
                intent=new Intent(mContext, PointListActivity.class);
                break;
            case R.id.insp_upload_problem_ib:
                intent=new Intent(mContext, UploadProblemActivity.class);
                break;
            case R.id.insp_notice_ib:
                intent=new Intent(mContext, NoticeListActivity.class);
                break;
            case R.id.insp_add_fire_ib:
                if(MyApp.getPrivilege()==11){
                    T.showShort(mContext,"该账号不具备该权限");
                }else{
                    intent = new Intent(mContext, ChioceDevTypeActivity.class);
                }
                break;
            case R.id.insp_upload_msg_ib:
                intent = new Intent(mContext, UploadMsgActivity.class);
                break;
            case R.id.host_info_ib:
                intent = new Intent(mContext, HostActivity.class);
                break;
            case R.id.camera_ib:
                intent = new Intent(mContext, CameraDevActivity.class);
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }
}
