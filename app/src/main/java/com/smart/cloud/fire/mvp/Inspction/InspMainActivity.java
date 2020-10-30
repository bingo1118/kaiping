package com.smart.cloud.fire.mvp.Inspction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.igexin.sdk.PushManager;
import com.smart.cloud.fire.activity.Inspection.InspectionMain.InspMainFragment;
import com.smart.cloud.fire.activity.Inspection.InspectionMain.InspSettingFragment;
import com.smart.cloud.fire.activity.Inspection.InspectionMap.InspMapFragment;
import com.smart.cloud.fire.activity.Inspection.ItemsList.ItemsListFragment;
import com.smart.cloud.fire.activity.Inspection.NoticeListActivity.NoticeFragment;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListFragment;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AllDevFragment.AllDevInspFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AssetManager.AssetHomeFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.CameraFragment.CameraV4Fragment;
import com.smart.cloud.fire.mvp.login.SplashActivity;
import com.smart.cloud.fire.mvp.main.MainInspFragment;
import com.smart.cloud.fire.mvp.main.presenter.MainPresenter;
import com.smart.cloud.fire.mvp.main.view.MainView;
import com.smart.cloud.fire.service.RemoteService;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspMainActivity extends MvpActivity<MainPresenter> implements MainView {

    @Bind(R.id.framePage)
    FrameLayout framePage;
    @Bind(R.id.navigation)
    BottomNavigationView navigation;

//    TaskListFragment f1;
    ItemsListFragment f1;
    InspMapFragment f2;
    CameraV4Fragment f3;
//    AssetHomeFragment f3;
    AllDevInspFragment f4;
    InspSettingFragment f5;

    Context mContext;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insp_main);
        ButterKnife.bind(this);
        mContext=this;

        //不为null，说明是死而复活，移除已经存在的fragment,处理重影问题
        if (savedInstanceState != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.remove(getFragmentManager().findFragmentByTag("F1"));
            transaction.remove(getFragmentManager().findFragmentByTag("F2"));
            transaction.remove(getFragmentManager().findFragmentByTag("F3"));
            transaction.remove(getFragmentManager().findFragmentByTag("F4"));
            transaction.remove(getFragmentManager().findFragmentByTag("f5"));
            transaction.commitAllowingStateLoss();
        }


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        regFilter();
        startService(new Intent(InspMainActivity.this, RemoteService.class));
        //启动个推接收推送信息。。
        PushManager.getInstance().initialize(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.smart.cloud.fire.geTuiPush.DemoIntentService.class);
        init();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    public void init(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            switch (item.getItemId()) {
                case R.id.insp_main:
                    if(f1==null){
                        f1 = new ItemsListFragment();
                        transaction.add(R.id.framePage,f1,"F1");
                    }else{
                        transaction.show(f1);
                    }
                    break;
                case R.id.insp_map:
                    if(f2==null){
                        f2 = new InspMapFragment();
                        transaction.add(R.id.framePage,f2,"F2");
                    }else{
                        transaction.show(f2);
                    }
                    break;
                case R.id.insp_camera:
                    if(f3==null){
                        f3 = new CameraV4Fragment();
                        transaction.add(R.id.framePage,f3,"F3");
                    }else{
                        transaction.show(f3);
                    }
                    break;
                case R.id.insp_fire:
                    if(f4==null){
                        f4 =new AllDevInspFragment();
                        transaction.add(R.id.framePage,f4,"F4");
                    }else{
                        transaction.show(f4);
                    }
                    break;
                case R.id.insp_notice:
                    if(f5==null){
                        f5 =new InspSettingFragment();
                        transaction.add(R.id.framePage,f5,"F5");
                    }else{
                        transaction.show(f5);
                    }
                    break;
            }
            transaction.commit();
            return true;
        }
    };

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
            f2.clearState();
        }
        if(f3!=null){
            transaction.hide(f3);
        }
        if(f4!=null){
            transaction.hide(f4);
            f4.clearData();
        }
        if(f5!=null){
            transaction.hide(f5);
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
