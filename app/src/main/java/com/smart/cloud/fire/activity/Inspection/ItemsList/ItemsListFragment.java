package com.smart.cloud.fire.activity.Inspection.ItemsList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.NFCInfoEntity;
import com.smart.cloud.fire.order.OrderInfoDetail.OrderInfoActivity;
import com.smart.cloud.fire.order.OrderList.OrderListActivity;
import com.smart.cloud.fire.order.OrderList.OrderListAdapter;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.view.BingoSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ItemsListFragment extends MvpFragment<ItemsListPresenter> implements ItemsListView{

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.sum_line)
    LinearLayout sum_line;
    @Bind(R.id.sum_tv)
    TextView sum_tv;
    @Bind(R.id.pass_tv)
    TextView pass_tv;
    @Bind(R.id.progress_tv)
    TextView progress_tv;
    @Bind(R.id.change)
    TextView change;
    @Bind(R.id.search_bingo)
    BingoSearchView search_bingo;
    @Bind(R.id.order_tv)
    TextView order_tv;
    @Bind(R.id.quick_deal_btn)
    Button quick_deal_btn;
    @Bind(R.id.nothing)
    ImageView nothing;

    private ItemsListPresenter mPresenter;
    private ItemsListAdapter mAdapter;
    Context mContext;

    String userID;

    String tasktype="";//任务类型 0/1/2 临时/日/月
    String state="";//巡检状态  1/2/3 待巡检/不合格/合格
    String tid="";

    @Override
    public void onResume() {
        super.onResume();
        mContext=getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        mPresenter.getAllItems(userID,state,tasktype);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_items_list, container, false);
        ButterKnife.bind(this, view);
//        mContext=getActivity();
//        userID = SharedPreferencesManager.getInstance().getData(mContext,
//                SharedPreferencesManager.SP_FILE_GWELL,
//                SharedPreferencesManager.KEY_RECENTNAME);
//        mPresenter.getAllItems(userID,state,tasktype);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state="";
                tasktype="";
                mPresenter.getAllItems(userID,state,tasktype);
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        order_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, OrderListActivity.class);
                mContext.startActivity(intent);
            }
        });
        search_bingo.setVisibility(View.GONE);
        quick_deal_btn.setVisibility(View.GONE);
        quick_deal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("提示")
                        .setMessage("是否确认一键处理？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                VolleyHelper helper = VolleyHelper.getInstance(mContext);
                                RequestQueue mQueue = helper.getRequestQueue();
                                String url = ConstantValues.SERVER_IP_NEW + "oneKeyHandle?userId="+userID;
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    int errorCode = response.getInt("errorCode");
                                                    String error = response.getString("error");
                                                    if (errorCode == 0) {
                                                        T.showShort(mContext, "成功");
                                                    } else {
                                                        T.showShort(mContext, error);
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
                        });
                builder.show();
            }
        });
        return view;
    }

    private PopupWindow popupWindow = null;
    String state_temp="";
    String tasktype_temp="";
    /**
     * 打开下拉列表弹窗
     */
    public void showPopWindow() {
        // 加载popupWindow的布局文件

        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater =  (LayoutInflater) mContext.getSystemService(infServie);
        View contentView  = layoutInflater.inflate(R.layout.choose_item_condition, null,false);

        RadioGroup radioGroup_status=(RadioGroup) contentView.findViewById(R.id.status_rg);
        radioGroup_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.status1_rb:
                        state_temp="1";
                        break;
                    case R.id.status2_rb:
                        state_temp="3";
                        break;
                    case R.id.status3_rb:
                        state_temp="2";
                        break;
                }
            }
        });

        RadioGroup radioGroup_tasktype=(RadioGroup) contentView.findViewById(R.id.tasktype_rg);
        radioGroup_tasktype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tasktype1_rb:
                        tasktype_temp="0";
                        break;
                    case R.id.tasktype2_rb:
                        tasktype_temp="1";
                        break;
                    case R.id.tasktype3_rb:
                        tasktype_temp="2";
                        break;
                }
            }
        });
        Button commit=(Button)contentView.findViewById(R.id.commit_btn);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=state_temp;
                tasktype=tasktype_temp;
                state_temp="";
                tasktype_temp="";
                mPresenter.getAllItems(userID,state,tasktype);
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable( R.drawable.list_item_color_bg));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow=null;
            }
        });//@@12.20
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
//        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                backgroundAlpha(1f);
                popupWindow = null;
            }
        });
        popupWindow.showAsDropDown(change);
    }


    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
            nothing.setVisibility(View.VISIBLE);
        }else{
            nothing.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemsListAdapter(mContext, pointList, mPresenter,tid);
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);

        sum_line.setVisibility(View.GONE);
    }

    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList, int sum, int pass, int checked) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
            nothing.setVisibility(View.VISIBLE);
        }else{
            nothing.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemsListAdapter(mContext, pointList, mPresenter,tid);
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);

        sum_line.setVisibility(View.VISIBLE);
        sum_tv.setText("总数:"+sum);
        progress_tv.setText("已检:"+checked);
        pass_tv.setText("合格:"+pass);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    protected ItemsListPresenter createPresenter() {
        mPresenter=new ItemsListPresenter(this);
        return mPresenter;
    }

    @Override
    public String getFragmentName() {
        return null;
    }


    //@@12.20
    public void backgroundAlpha(float bgAlpha)
    {

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }
}
