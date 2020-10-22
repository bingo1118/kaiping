package com.smart.cloud.fire.activity.Inspection.TaskList;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.Inspection.ItemsList.ItemsListActivity;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.InspectionTask;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.MyRadioGroup;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class TaskListFragment extends MvpFragment<TaskListPresenter> implements TaskListView,View.OnClickListener{

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.null_data_iv)
    TextView null_data_iv;

    @Bind(R.id.select_btn)
    ImageButton select_btn;


    @Bind(R.id.sum)
    TextView sum;
    @Bind(R.id.pro)
    TextView pro;
    @Bind(R.id.complish)
    TextView complish;


    private Context mContext;
    private TaskListPresenter mPresenter;
    private TaskListAdapter mAdapter;

    boolean isSelectLineShow=false;

    String userID;

    String state="";
    String type="";
    String startTime="";
    String endTime="";

    int mYear, mMonth, mDay;
    final int DATE_DIALOG = 1;
    int startOrEnd=0;
    private PopupWindow popupWindow;
    private ViewHolder viewHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_list, container, false);
        ButterKnife.bind(this, view);
        mContext=getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        mPresenter.getTasks(userID,"","","","");
        mPresenter.getTasksNum(userID);

        initView();

        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    private void initView() {
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getTasks(userID,"","","","");
                mPresenter.getTasksNum(userID);
            }
        });

    }

    private void clearData() {
        state="";
        type="";
        startTime="";
        endTime="";
        viewHolder.task_type_rg.clearCheck();
        viewHolder.task_state_rg.clearCheck();
        viewHolder.starttime_tv.setText("");
        viewHolder.endtime_tv.setText("");

    }

    @Override
    public void onClick(View v) {
        // 直接将onClick监听事件转到ButterKnife的onViewClicked中处理
        this.onClick1(v);
    }

    @OnClick({R.id.select_btn})
    public void onClick1(View view){
        DatePickerDialog datePickerDialog;
        switch (view.getId()){
            case R.id.starttime_tv:
                 datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mMonth = month;
                                mDay = dayOfMonth;
                                display();
                            }
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();
                startOrEnd=1;
                break;
            case R.id.endtime_tv:
                 datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mMonth = month;
                                mDay = dayOfMonth;
                                display();
                            }
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();
                startOrEnd=2;
                break;
            case R.id.commit_tv:
                if(viewHolder.task_type0.isChecked()){
                    type="0";
                }else if(viewHolder.task_type1.isChecked()){
                    type="1";
                }else if(viewHolder.task_type2.isChecked()){
                    type="2";
                }else if(viewHolder.task_type3.isChecked()){
                    type="3";
                }else if(viewHolder.task_type4.isChecked()){
                    type="4";
                }
                if(viewHolder.task_state0.isChecked()){
                    state="0";
                }else if(viewHolder.task_state1.isChecked()){
                    state="2";
                }else if(viewHolder.task_state2.isChecked()){
                    state="3";
                }
                if(!((startTime=viewHolder.starttime_tv.getText().toString()).length()>5)){
                    startTime="";
                }
                if(!((endTime=viewHolder.endtime_tv.getText().toString()).length()>5)){
                    endTime="";
                }
                mPresenter.getTasks(userID,type,state,startTime,endTime);
                clearData();
//                viewHolder.select_line.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.select_btn:
                if(popupWindow==null){
                    View popupView=LayoutInflater.from(mContext).inflate(R.layout.tasklist_popup,null);
                    viewHolder=new ViewHolder(popupView);
                    popupWindow=new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setTouchable(true);
                    viewHolder.starttime_tv.setOnClickListener(this);
                    viewHolder.endtime_tv.setOnClickListener(this);
                    viewHolder.commit_tv.setOnClickListener(this);
                }
                if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else{
                    popupWindow.showAsDropDown(select_btn);
                }
//                if(!isSelectLineShow){
//                    select_line.setVisibility(View.VISIBLE);
//                    isSelectLineShow=true;
//                }else{
//                    select_line.setVisibility(View.GONE);
//                    isSelectLineShow=false;
//                    clearData();
//                }
                break;
        }
    }

    @Override
    protected TaskListPresenter createPresenter() {
        if(mPresenter==null)
            mPresenter=new TaskListPresenter(this);
        return mPresenter;
    }


    @Override
    public void getDataSuccess(List<InspectionTask> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
            null_data_iv.setVisibility(View.VISIBLE);
        }else{
            null_data_iv.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new TaskListAdapter(mContext, pointList, mPresenter);
        mAdapter.setOnRecyclerViewItemClickListener(new TaskListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, InspectionTask data) {
                Intent intent=new Intent(mContext, ItemsListActivity.class);
                intent.putExtra("pid",data.getPid());
                intent.putExtra("tid",data.getTid());
                startActivity(intent);
            }
        });
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void getSumDataSuccess(int todoNum, int progreaNum, int complishNum) {
        sum.setText("未开始:"+todoNum);
        pro.setText("进行中:"+progreaNum);
        complish.setText("已完成"+complishNum);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
        null_data_iv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }


    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        if(startOrEnd==1){
            viewHolder.starttime_tv.setText(new StringBuffer()
                    .append(mYear)
                    .append("-").append((mMonth + 1)<10?"0"+(mMonth + 1):(mMonth + 1))
                    .append("-").append(mDay<10?"0"+mDay:mDay));
        }else if(startOrEnd==2){
            viewHolder.endtime_tv.setText(new StringBuffer()
                    .append(mYear)
                    .append("-").append((mMonth + 1)<10?"0"+(mMonth + 1):(mMonth + 1))
                    .append("-").append(mDay<10?"0"+mDay:mDay));
        }
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    static class ViewHolder{
        @Bind(R.id.select_line)
        RelativeLayout select_line;
        @Bind(R.id.task_type_rg)
        MyRadioGroup task_type_rg;
        @Bind(R.id.task_type0)
        RadioButton task_type0;
        @Bind(R.id.task_type1)
        RadioButton task_type1;
        @Bind(R.id.task_type2)
        RadioButton task_type2;
        @Bind(R.id.task_type3)
        RadioButton task_type3;
        @Bind(R.id.task_type4)
        RadioButton task_type4;

        @Bind(R.id.task_state_rg)
        MyRadioGroup task_state_rg;
        @Bind(R.id.task_state0)
        RadioButton task_state0;
        @Bind(R.id.task_state1)
        RadioButton task_state1;
        @Bind(R.id.task_state2)
        RadioButton task_state2;

        @Bind(R.id.starttime_tv)
        TextView starttime_tv;
        @Bind(R.id.endtime_tv)
        TextView endtime_tv;

        @Bind(R.id.commit_tv)
        TextView commit_tv;

        public ViewHolder(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

}
