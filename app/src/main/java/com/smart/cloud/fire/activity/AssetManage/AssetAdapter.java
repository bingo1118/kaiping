package com.smart.cloud.fire.activity.AssetManage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.AssetInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class AssetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ACheck> itemsList;
    private AssetPresenter mPresenter;
    private AssetAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ACheck data);
    }

    public void setOnRecyclerViewItemClickListener(AssetAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public AssetAdapter(Context mContext, List<ACheck> electricList,AssetPresenter presenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.itemsList = electricList;
        this.mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.asset_info_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        AssetAdapter.ItemViewHolder viewHolder = new AssetAdapter.ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ACheck mPoint = itemsList.get(position);
        ((ItemViewHolder) holder).name_tv.setText(mPoint.getNamed());
        ((ItemViewHolder) holder).time_tv.setText("盘点时间:"+mPoint.getAddTime());
        ((ItemViewHolder) holder).area_tv.setText("盘点区域:"+mPoint.getArea());
        ((ItemViewHolder) holder).type_tv.setText("资产类别:"+mPoint.getPnamed()+"-"+mPoint.getApnamed());
        ((ItemViewHolder) holder).conpleted_tv.setText("已完成:"+mPoint.getFinish());
        ((ItemViewHolder) holder).no_conplete_tv.setText("未完成:"+mPoint.getUnFinish());
        ((ItemViewHolder) holder).state_tv.setText(mPoint.getStateName());
        ((ItemViewHolder) holder).memo.setText("备注:"+mPoint.getMemo());


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
            mOnItemClickListener.onItemClick(v, (ACheck) v.getTag());
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView name_tv;
        @Bind(R.id.state_tv)
        TextView state_tv;
        @Bind(R.id.time_tv)
        TextView time_tv;
        @Bind(R.id.area_tv)
        TextView area_tv;
        @Bind(R.id.type_tv)
        TextView type_tv;
        @Bind(R.id.conpleted_tv)
        TextView conpleted_tv;
        @Bind(R.id.no_conplete_tv)
        TextView no_conplete_tv;
        @Bind(R.id.memo_tv)
        TextView memo;
        @Bind(R.id.rela)
        RelativeLayout rela;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
