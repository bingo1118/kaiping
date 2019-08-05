package com.smart.cloud.fire.activity.Inspection.InspectionMain;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.igexin.sdk.PushManager;
import com.smart.cloud.fire.activity.Camera.CameraDevActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNFCItemActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNormalItemActivity;
import com.smart.cloud.fire.activity.Inspection.InspectionMap.InspectionMapActivity;
import com.smart.cloud.fire.activity.Inspection.NoticeListActivity.NoticeListActivity;
import com.smart.cloud.fire.activity.Inspection.PointList.PointListActivity;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadInspectionInfoActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadProblemActivity;
import com.smart.cloud.fire.activity.Setting.MyZoomActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.login.SplashActivity;
import com.smart.cloud.fire.mvp.main.Main3Activity;
import com.smart.cloud.fire.mvp.main.presenter.MainPresenter;
import com.smart.cloud.fire.mvp.main.view.MainView;
import com.smart.cloud.fire.service.RemoteService;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspectionMainActivity extends MvpActivity<MainPresenter> implements MainView {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_main);

        ButterKnife.bind(this);
        mContext=this;

        regFilter();
        startService(new Intent(InspectionMainActivity.this, RemoteService.class));
        //启动个推接收推送信息。。
        PushManager.getInstance().initialize(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoIntentService.class);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @OnClick({R.id.insp_add_normal_ib,R.id.insp_add_nfc_ib,R.id.insp_task_ib
            ,R.id.insp_points_ib,R.id.insp_map_ib,R.id.insp_upload_problem_ib
            ,R.id.insp_quickly_ib,R.id.insp_fire,R.id.insp_camera
            ,R.id.my_image,R.id.insp_notice_ib})
    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.my_image:
                intent = new Intent(mContext, MyZoomActivity.class);
                break;
            case R.id.insp_add_normal_ib:
                intent=new Intent(this, AddInspectionNormalItemActivity.class);
                break;
            case R.id.insp_add_nfc_ib:
                intent=new Intent(this, AddInspectionNFCItemActivity.class);
                break;
            case R.id.insp_task_ib:
                intent=new Intent(this, TaskListActivity.class);
                break;
            case R.id.insp_points_ib:
                intent=new Intent(this, PointListActivity.class);
                break;
            case R.id.insp_map_ib:
                intent=new Intent(this, InspectionMapActivity.class);
                break;
            case R.id.insp_upload_problem_ib:
                intent=new Intent(this, UploadProblemActivity.class);
                break;
            case R.id.insp_quickly_ib:
                intent=new Intent(this, UploadInspectionInfoActivity.class);
                break;
            case R.id.insp_fire:
                intent=new Intent(this, Main3Activity.class);
                break;
            case R.id.insp_camera:
                intent=new Intent(this, CameraDevActivity.class);
                break;
            case R.id.insp_notice_ib:
                intent=new Intent(this, NoticeListActivity.class);
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }


    /**
     * 添加广播接收器件。。
     */
    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("APP_EXIT");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            //退出。。
            if (intent.getAction().equals("APP_EXIT")) {
                SharedPreferencesManager.getInstance().putData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTPASS,
                        "");
                SharedPreferencesManager.getInstance().removeData(mContext,
                        "LASTAREANAME");//@@11.13
                SharedPreferencesManager.getInstance().removeData(mContext,
                        "LASTAREAID");//@@11.13
                SharedPreferencesManager.getInstance().removeData(mContext,
                        "LASTAREAISPARENT");//@@11.13
                PushManager.getInstance().stopService(getApplicationContext());
                unbindAlias();
                Intent in = new Intent(mContext, SplashActivity.class);
                startActivity(in);
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
        VolleyHelper.getInstance(mContext).stopRequestQueue();
    }

    /**
     * 个推解绑@@5.16
     */
    private void unbindAlias() {
        String userCID = SharedPreferencesManager.getInstance().getData(this,SharedPreferencesManager.SP_FILE_GWELL,"CID");//@@
        String username = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        String url= ConstantValues.SERVER_IP_NEW+"loginOut?userId="+username+"&alias="+username+"&cid="+userCID+"&appId=1";//@@5.27添加app编号
//        RequestQueue mQueue = Volley.newRequestQueue(this);
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mvpPresenter.exitBy2Click(mContext);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void exitBy2Click(boolean isExit) {
        if (isExit) {
            //moveTaskToBack(false);
            moveTaskToBack(true);//@@5.31
        }
    }

    @Override
    public void getOnlineSummary(SmokeSummary model) {

    }

    @Override
    public void getSafeScore(SafeScore model) {

    }
}
