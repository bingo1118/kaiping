package com.smart.cloud.fire.activity.AssetManage.Tag;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.activity.AssetManage.TagAlarm.TagAlarmInfo;
import com.smart.cloud.fire.activity.AssetManage.TagAlarm.TagAlarmPushActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TagListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<Smoke> itemsList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public List<String> getmList() {
        return mList;
    }

    private List<String> mList;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Smoke data);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public TagListAdapter(Context mContext, List<Smoke> electricList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.itemsList = electricList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.asset_list_item2, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Smoke mPoint = itemsList.get(position);

        ((ItemViewHolder) holder).name_tv.setText(mPoint.getName());
        ((ItemViewHolder) holder).mac_tv.setText("ID:"+mPoint.getMac());
        ((ItemViewHolder) holder).area_tv.setText("区域:"+mPoint.getAreaName());
        ((ItemViewHolder) holder).type_tv.setText("报警状态:"+mPoint.getIfAlarmName());

        ((ItemViewHolder) holder).state_tv.setVisibility(View.GONE);
        ((ItemViewHolder) holder).state_tv.setText(mPoint.getNetState()+"");
        String state="";
        switch (mPoint.getNetState()){
            case 0:
                state="离线";
                break;
            case 1:
                state="在线";
                break;
        }
        ((ItemViewHolder) holder).state2_tv.setText("状态:"+state);
        ((ItemViewHolder) holder).commit_tv.setVisibility(View.GONE);

        ((ItemViewHolder) holder).rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.itemView.setTag(mPoint);
    }

    @Override
    public int getItemCount() {

        return itemsList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Smoke) v.getTag());
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView name_tv;
        @Bind(R.id.state_tv)
        TextView state_tv;
        @Bind(R.id.mac_tv)
        TextView mac_tv;
        @Bind(R.id.area_tv)
        TextView area_tv;
        @Bind(R.id.type_tv)
        TextView type_tv;
        @Bind(R.id.state2_tv)
        TextView state2_tv;
        @Bind(R.id.commit_tv)
        TextView commit_tv;
        @Bind(R.id.rela)
        RelativeLayout rela;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
