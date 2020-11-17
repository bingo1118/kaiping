package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.AllDevFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smart.cloud.fire.activity.AlarmHistory.AlarmHistoryActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeActivity;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokePresenter;
import com.smart.cloud.fire.activity.AllSmoke.AllSmokeView;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListFragment;
import com.smart.cloud.fire.adapter.ShopSmokeAdapter;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.ChuangAn.ChuangAnActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentView;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.view.BingoDropDowmListView;
import com.smart.cloud.fire.view.MyRadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class AllDevInspFragment extends MvpFragment<AllSmokePresenter> implements AllSmokeView,View.OnClickListener {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.select_btn)
    ImageButton select_btn;
    @Bind(R.id.title_rela)
    RelativeLayout title_rela;

    @Bind(R.id.select_line)
    RelativeLayout select_line;
    @Bind(R.id.dev_type_rg)
    MyRadioGroup dev_type_rg;
    @Bind(R.id.dev_type0)
    RadioButton dev_type0;
    @Bind(R.id.dev_type1)
    RadioButton dev_type1;
    @Bind(R.id.dev_type2)
    RadioButton dev_type2;
    @Bind(R.id.dev_type3)
    RadioButton dev_type3;

    @Bind(R.id.dev_state_rg)
    MyRadioGroup dev_state_rg;
    @Bind(R.id.dev_state0)
    RadioButton dev_state0;
    @Bind(R.id.dev_state1)
    RadioButton dev_state1;
    @Bind(R.id.dev_state2)
    RadioButton dev_state2;
    @Bind(R.id.dev_state3)
    RadioButton dev_state3;

    @Bind(R.id.devtype_drop)
    BingoDropDowmListView devtype_drop;//选择区域

    @Bind(R.id.commit_tv)
    TextView commit_tv;

    private LinearLayoutManager linearLayoutManager;
    private ShopSmokeAdapter shopSmokeAdapter;
    private int lastVisibleItem;
    private Context mContext;
    private List<Smoke> list;
    private int loadMoreCount;
    private boolean research = false;
    private String page;
    private String userID;
    private int privilege;
    private AllSmokePresenter mShopInfoFragmentPresenter;

    private boolean isSelectLineShow=false;

    String state="";
    String type="";
    String areaid="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_dev_insp, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege2();
        page = "1";
        list = new ArrayList<>();

        refreshListView();
        devtype_drop.setEditTextHint("区域");
        title_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });
        devtype_drop.initData("");
        mvpPresenter.getAllDev(userID, privilege + "", page,"","","", list, 1,false);
        mvpPresenter.getSmokeSummary(userID,privilege+"","","","","1");//@@9.5
    }

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipereFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipereFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipereFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //下拉刷新。。
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = "1";
                list.clear();
                research=false;
                mvpPresenter.getAllDev(userID, privilege + "", page,"","","", list, 1,false);
                mvpPresenter.getSmokeSummary(userID,privilege+"","","","","1");
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int count = shopSmokeAdapter.getItemCount();
                if(count<20){
                    return;
                }
//                if(research){//@@9.5 条件查询分页
//                    if(shopSmokeAdapter==null){
//                        return;
//                    }
//
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
//                        if(loadMoreCount>=20){
//                            page = Integer.parseInt(page) + 1 + "";
//                            mvpPresenter.getAllDev(userID, privilege + "", page,"","","", list, 1,true);
//                        }else{
//                            T.showShort(mContext,"已经没有更多数据了");
//                        }
//                    }
//                    return;
//                }//@@9.5
                if(shopSmokeAdapter==null){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == count) {
                    if(loadMoreCount>=20){
                        page = Integer.parseInt(page) + 1 + "";
                        if(research){
                            mvpPresenter.getAllDev(userID, privilege + "", page,type,state,areaid, list, 1,true);
                        }else{
                            mvpPresenter.getAllDev(userID, privilege + "", page,"","","", list, 1,false);
                        }
                    }else{
                        T.showShort(mContext,"已经没有更多数据了");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void refreshView() {
        page = "1";
        list.clear();
        mvpPresenter.getAllSmoke(userID, privilege + "", page,"1", list, 1,true);
        mvpPresenter.getSmokeSummary(userID,privilege+"","","","","1");
    }

    @Override
    public void onClick(View v) {
        // 直接将onClick监听事件转到ButterKnife的onViewClicked中处理
        this.onClick1(v);
    }

    @OnClick({R.id.select_btn,R.id.alarm_history_tv})
    public void onClick1(View view){
        switch (view.getId()){
            case R.id.alarm_history_tv:
                Intent i=new Intent(mContext, AlarmHistoryActivity.class);
                mContext.startActivity(i);
                break;
            case R.id.commit_tv:
                if(dev_type0.isChecked()){
                    type="1";
                }else if(dev_type1.isChecked()){
                    type="4";
                }else if(dev_type2.isChecked()){
                    type="3";
                }
                if(dev_state0.isChecked()){
                    state="0";
                }else if(dev_state1.isChecked()){
                    state="1";
                }else if(dev_state2.isChecked()){
                    state="2";
                }else if(dev_state3.isChecked()){
                    state="3";
                }
                areaid=devtype_drop.getSelecedId()==null?"":devtype_drop.getSelecedId();
                page="1";
                mvpPresenter.getAllDev(userID, privilege + "", page,type,state,areaid, list, 1,true);
//                viewHolder.select_line.setVisibility(View.GONE);
                clearData();
                break;
            case R.id.select_btn:
                select_line.setVisibility(View.VISIBLE);
                commit_tv.setOnClickListener(this);
                if(!isSelectLineShow){
                    select_line.setVisibility(View.VISIBLE);
                    select_line.setFocusable(true);
                    isSelectLineShow=true;
                }else{
                    select_line.setVisibility(View.GONE);
                    select_line.setFocusable(false);
                    isSelectLineShow=false;
                    clearData();
                }
                break;
        }
    }

    public void clearData() {
        select_line.setFocusable(false);
        select_line.setVisibility(View.GONE);
        dev_type_rg.clearCheck();
        dev_state_rg.clearCheck();
        isSelectLineShow=false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected AllSmokePresenter createPresenter() {
        mShopInfoFragmentPresenter = new AllSmokePresenter(this);
        return mShopInfoFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "AllDevFragment";
    }

    @Override
    public void getDataSuccess(List<?> smokeList,boolean search) {
        research = search;
        if(search!=false&&!page.equals("1")){
            page="1";
        }//@@9.5
        loadMoreCount = smokeList.size();
        if(loadMoreCount==0){
            T.showShort(mContext,"无数据");
        }
        list.clear();
        list.addAll((List<Smoke>)smokeList);
        shopSmokeAdapter = new ShopSmokeAdapter(mContext, list);
        shopSmokeAdapter.setOnLongClickListener(new ShopSmokeAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Smoke smoke =list.get(position);
                if(smoke.getDeviceType()==22||smoke.getDeviceType()==23||smoke.getDeviceType()==58||smoke.getDeviceType()==61
                        ||smoke.getDeviceType()==73||smoke.getDeviceType()==75||smoke.getDeviceType()==77
                        ||smoke.getDeviceType()==78||smoke.getDeviceType()==79||smoke.getDeviceType()==80
                        ||smoke.getDeviceType()==83||smoke.getDeviceType()==85||smoke.getDeviceType()==86
                        ||smoke.getDeviceType()==87||smoke.getDeviceType()==89||smoke.getDeviceType()==90
                        ||smoke.getDeviceType()==91||smoke.getDeviceType()==92||smoke.getDeviceType()==93
                        ||smoke.getDeviceType()==94||smoke.getDeviceType()==95){
                    showNormalDialog(smoke.getMac(),smoke.getDeviceType(),position);
                }else{
                    T.showShort(mContext,"该设备无法删除");
                }
            }
        });
        shopSmokeAdapter.setOnClickListener(new ShopSmokeAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Smoke smoke=list.get(position);
                switch (smoke.getDeviceType()){
                    case 51://@@创安燃气
                        Intent intent = new Intent(mContext, ChuangAnActivity.class);
                        intent.putExtra("Mac",smoke.getMac());
                        intent.putExtra("Position",smoke.getName());
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
        recyclerView.setAdapter(shopSmokeAdapter);
        swipereFreshLayout.setRefreshing(false);
//        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
    }

    private void showNormalDialog(final String mac, final int deviceType, final int position){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("确认删除该设备?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VolleyHelper helper=VolleyHelper.getInstance(mContext);
                        RequestQueue mQueue = helper.getRequestQueue();
                        String userid= SharedPreferencesManager.getInstance().getData(mContext,
                                SharedPreferencesManager.SP_FILE_GWELL,
                                SharedPreferencesManager.KEY_RECENTNAME);
                        String url="";
                        switch (deviceType){
                            case 58:
                                url= ConstantValues.SERVER_IP_NEW+"deleteOneNetDevice?imei="+mac;
                                break;
                            default:
                                url= ConstantValues.SERVER_IP_NEW+"deleteDeviceById?imei="+mac;
                                break;
                        }
                        StringRequest stringRequest = new StringRequest(url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject=new JSONObject(response);
                                            int errorCode=jsonObject.getInt("errorCode");
                                            if(errorCode==0){
                                                list.remove(position);
                                                shopSmokeAdapter.notifyDataSetChanged();
                                                T.showShort(mContext,"删除成功");
                                            }else{
                                                T.showShort(mContext,"删除失败");
                                            }
                                            T.showShort(mContext,jsonObject.getString("error"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            T.showShort(mContext,"删除失败");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", error.getMessage(), error);
                                T.showShort(mContext,"删除失败");
                            }
                        });
                        mQueue.add(stringRequest);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
        swipereFreshLayout.setRefreshing(false);
        if(shopSmokeAdapter!=null){
            shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.NO_DATA);
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingMore(List<?> smokeList) {
        loadMoreCount = smokeList.size();
        list.addAll((List<Smoke>)smokeList);
        shopSmokeAdapter.changeMoreStatus(ShopSmokeAdapter.LOADING_MORE);
    }

    @Override
    public void refreshFragment() {

    }

    @Override
    public void getAreaType(ArrayList<?> shopTypes, int type) {
    }

    @Override
    public void getAreaTypeFail(String msg, int type) {
    }

    @Override
    public void unSubscribe(String type) {
    }


    @Override
    public void getChoiceArea(Area area) {

    }

    @Override
    public void getChoiceShop(ShopType shopType) {

    }

    @Override
    public void getSmokeSummary(SmokeSummary smokeSummary) {
//        totalNum.setText(smokeSummary.getAllSmokeNumber()+"");
//        onlineNum.setText(smokeSummary.getOnlineSmokeNumber()+"");
//        offlineNum.setText(smokeSummary.getLossSmokeNumber()+"");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}

