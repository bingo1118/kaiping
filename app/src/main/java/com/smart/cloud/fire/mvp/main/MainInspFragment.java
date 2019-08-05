package com.smart.cloud.fire.mvp.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.igexin.sdk.PushManager;
import com.p2p.core.P2PHandler;
import com.smart.cloud.fire.activity.AddDev.ChioceDevTypeActivity;
import com.smart.cloud.fire.activity.AlarmHistory.AlarmHistoryActivity;
import com.smart.cloud.fire.activity.AlarmMsg.AlarmMsgActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.activity.Camera.CameraDevActivity;
import com.smart.cloud.fire.activity.Electric.ElectricDevActivity;
import com.smart.cloud.fire.activity.Functions.FunctionsActivity;
import com.smart.cloud.fire.activity.Functions.constant.Constant;
import com.smart.cloud.fire.activity.Functions.model.ApplyTable;
import com.smart.cloud.fire.activity.Functions.util.ACache;
import com.smart.cloud.fire.activity.Functions.util.ApplyTableManager;
import com.smart.cloud.fire.activity.Host.HostActivity;
import com.smart.cloud.fire.activity.Inspection.InspectionMain.InspectionMainActivity;
import com.smart.cloud.fire.activity.NFCDev.NFCDevActivity;
import com.smart.cloud.fire.activity.SecurityDev.SecurityDevActivity;
import com.smart.cloud.fire.activity.Setting.MyZoomActivity;
import com.smart.cloud.fire.activity.WiredDev.WiredDevActivity;
import com.smart.cloud.fire.adapter.MyRecyclerViewAdapter;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MainService;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.BigData.BigDataActivity;
import com.smart.cloud.fire.mvp.login.SplashActivity;
import com.smart.cloud.fire.mvp.main.presenter.MainPresenter;
import com.smart.cloud.fire.mvp.main.view.MainView;
import com.smart.cloud.fire.ui.view.CircleProgressBar;
import com.smart.cloud.fire.ui.view.ItemDivider;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.yoosee.P2PListener;
import com.smart.cloud.fire.yoosee.SettingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class MainInspFragment extends MvpFragment<MainPresenter> implements MainView {

    private MyRecyclerViewAdapter myAdapte1r;
    Context mContext;

    @Bind(R.id.home_alarm_light)
    ImageView home_alarm_light;
    @Bind(R.id.home_alarm_info_text)
    TextView home_alarm_info_text;
    @Bind(R.id.dev_sum)
    TextView dev_sum;
    @Bind(R.id.offline_sum)
    TextView offline_sum;
    @Bind(R.id.fault_sum)
    TextView fault_sum;
    @Bind(R.id.alarm_sum)
    TextView alarm_sum;
    @Bind(R.id.scan_btn)
    Button scan_btn;
    @Bind(R.id.circleProgressBar)
    CircleProgressBar circleProgressBar;
    @Bind(R.id.message_notice_list_item)
    RecyclerView recyclerView;



    Timer getlastestAlarm;
    AnimationDrawable anim ;

    MainPresenter presenter;
    ArrayList<ApplyTable> list;
    private int privilege;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main3, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();

        privilege = MyApp.app.getPrivilege();

        dealWithScan();
        initView();
        anim = (AnimationDrawable) home_alarm_light.getBackground();
        }

    @Override
    public String getFragmentName() {
        return "";
    }

    private void initView() {
        getHistoryCore();

        String a = Constant.APPLY_MINE;
        list = (ArrayList<ApplyTable>) ACache.get(MyApp.app).getAsObject(Constant.APPLY_MINE);
        if(list==null){
            list= (ArrayList<ApplyTable>) ApplyTableManager.loadNewsChannelsStatic(privilege);
            ACache.get(MyApp.app).put(Constant.APPLY_MINE,list);
        }
        if(privilege==31||privilege==32||privilege==4||privilege==6||privilege==61||privilege==7){
            ApplyTable editModel=new ApplyTable("更多功能","11",11,false, "bianji.png",1);
            list.add(editModel);
            scan_btn.setVisibility(View.VISIBLE);
            circleProgressBar.setVisibility(View.VISIBLE);
        }else{
            circleProgressBar.setVisibility(View.INVISIBLE);
            scan_btn.setVisibility(View.GONE);
        }


        myAdapte1r = new MyRecyclerViewAdapter(list);

        myAdapte1r.setItemClickListener(new MyRecyclerViewAdapter.MyItemClickListener() {
            Intent intent;
            @Override
            public void onItemClick(View view, int position) {
                ApplyTable table=list.get(position);
                switch (Integer.parseInt(table.getId())){
                    case 0:
                        intent = new Intent(mContext, ChioceDevTypeActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(mContext, AllSmokeActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(mContext, WiredDevActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(mContext, ElectricDevActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(mContext, SecurityDevActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(mContext, CameraDevActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(mContext, NFCDevActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent= new Intent(mContext, HostActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent=new Intent(mContext, InspectionMainActivity.class);
                        startActivity(intent);
                        break;
                    case 11:
                        intent=new Intent(mContext, FunctionsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        //纵向线性布局
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,4);
        recyclerView.addItemDecoration(new ItemDivider().setDividerWith(2).setDividerColor(0xe5e5e5));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapte1r);

        P2PHandler.getInstance().p2pInit(mContext,
                new P2PListener(),
                new SettingListener());
        getlastestAlarm=new Timer();
        getlastestAlarm.schedule(new TimerTask() {
            @Override
            public void run() {
                String username = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);

                String url= ConstantValues.SERVER_IP_NEW+"getLastestAlarm?userId="+username+"&privilege="+privilege;
                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
//                RequestQueue mQueue = Volley.newRequestQueue(MyApp.app);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int errorCode=response.getInt("errorCode");
                                    if(errorCode==0){
                                        JSONObject lasteatalarm=response.getJSONObject("lasteatAlarm");
                                        if(lasteatalarm.getInt("ifDealAlarm")==0){
                                            anim.start();
                                            home_alarm_info_text.setText(lasteatalarm.getString("address")+"\n"+lasteatalarm.getString("name")+"发生报警");
                                        }else{
                                            anim.stop();
                                            home_alarm_info_text.setText(lasteatalarm.getString("address")
                                                    +"\n"+lasteatalarm.getString("name")+"发生报警【已处理】");
                                        }
                                    }else{
                                        anim.stop();
                                        home_alarm_info_text.setText("无最新报警信息");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        anim.stop();
                        home_alarm_info_text.setText("未获取到数据");
                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        },0,10000);
    }


    /**
     * 个推解绑@@5.16
     */
    private void unbindAlias() {
        String userCID = SharedPreferencesManager.getInstance().getData(mContext,SharedPreferencesManager.SP_FILE_GWELL,"CID");//@@
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

    private void dealWithScan() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int privilege = MyApp.app.getPrivilege();
                String userID = SharedPreferencesManager.getInstance().getData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME);
                presenter.getSmokeSummary(userID,privilege+"","","","","");
            }
        };
        timer.schedule(timerTask,5000);
    }

    @OnClick({R.id.scan_btn,R.id.my_image,R.id.alarm_msg,R.id.alarm_line,R.id.circleProgressBar})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.scan_btn:
                intent=new Intent(mContext, BigDataActivity.class);
                startActivity(intent);
                break;
            case R.id.alarm_msg:
                intent=new Intent(mContext, AlarmMsgActivity.class);
                startActivity(intent);
                break;
            case R.id.my_image:
                intent = new Intent(mContext, MyZoomActivity.class);
                startActivity(intent);
                break;
            case R.id.alarm_line:
                intent = new Intent(mContext, AlarmHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.circleProgressBar:
                getHistoryCore();
                break;
            default:
                break;
        }
    }

    private void getHistoryCore() {
        String username = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        String url= ConstantValues.SERVER_IP_NEW+"getHistorSafeScore?userId="+username;
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("errorCode")==0){
                                circleProgressBar.setProgress(response.getInt("safeScore"), true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext,"获取历史分数错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @Override
    protected MainPresenter createPresenter() {
        presenter=new MainPresenter(this);
        return presenter;
    }


    @Override
    public void exitBy2Click(boolean isExit) {

    }

    @Override
    public void getOnlineSummary(SmokeSummary model) {
        dev_sum.setText((model.getAllSmokeNumber()-model.getLossSmokeNumber())+"");
        offline_sum.setText(model.getLossSmokeNumber()+"");
        fault_sum.setText(model.getLowVoltageNumber()+"");
        alarm_sum.setText(model.getAlarmDevNumber()+"");
    }

    @Override
    public void getSafeScore(SafeScore model) {
        if(model!=null){
            circleProgressBar.setProgress((int) model.getSafeScore(), true);
        }else{
            T.showShort(mContext,"获取评分失败");
        }
    }

}
