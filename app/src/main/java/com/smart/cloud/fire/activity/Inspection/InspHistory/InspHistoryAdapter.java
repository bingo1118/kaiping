package com.smart.cloud.fire.activity.Inspection.InspHistory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.UpdateItemInfoActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadInspectionInfoActivity;
import com.smart.cloud.fire.global.NFCInfoEntity;
import com.smart.cloud.fire.global.Point;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<InspHistoryEntity> itemsList;
    
    

    public InspHistoryAdapter(Context mContext, List<InspHistoryEntity> electricList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.itemsList = electricList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.insp_history_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        InspHistoryAdapter.ItemViewHolder viewHolder = new InspHistoryAdapter.ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InspHistoryEntity mPoint = itemsList.get(position);
        ((InspHistoryAdapter.ItemViewHolder) holder).name_tv.setText(mPoint.getTuid());
        ((InspHistoryAdapter.ItemViewHolder) holder).state_tv.setVisibility(View.VISIBLE);
        ((ItemViewHolder) holder).time_tv.setText("时间:"+mPoint.getChecktime());
        ((ItemViewHolder) holder).worker_tv.setText("巡检人:"+mPoint.getWorkerName());
        ((InspHistoryAdapter.ItemViewHolder) holder).state_tv.setText("状态:"+mPoint.getQualified());

        holder.itemView.setTag(mPoint);
    }

    @Override
    public int getItemCount() {

        return itemsList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView name_tv;
        @Bind(R.id.state_tv)
        TextView state_tv;
        @Bind(R.id.time_tv)
        TextView time_tv;
        @Bind(R.id.worker_tv)
        TextView worker_tv;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
