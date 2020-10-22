package com.smart.cloud.fire.activity.AssetManage.AssetByCkey;

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

import com.smart.cloud.fire.activity.AssetManage.DealAsset.DealAssetActivity;
import com.smart.cloud.fire.activity.AssetManage.DealAsset.UpdateAssetDataActivity;
import com.smart.cloud.fire.activity.AssetManage.DealAsset.UploadAssetProblemActivity;
import com.smart.cloud.fire.global.AssetInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class AssetByCkeyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<AssetByCkeyEntity> itemsList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
        notifyDataSetChanged();
        mList=new ArrayList<>();
    }

    private boolean showCheck=false;

    public List<String> getmList() {
        return mList;
    }

    private List<String> mList;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, AssetByCkeyEntity data);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public AssetByCkeyAdapter(Context mContext, List<AssetByCkeyEntity> electricList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.itemsList = electricList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.asset_list_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AssetByCkeyEntity mPoint = itemsList.get(position);

        if(showCheck){
            ((ItemViewHolder) holder).checkbox.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mList.add(mPoint.getId());
                    }else{
                        mList.remove(mPoint.getId());
                    }
                }
            });
        }else{
            ((ItemViewHolder) holder).checkbox.setVisibility(View.GONE);
        }


        ((ItemViewHolder) holder).name_tv.setText(mPoint.getNamed());
        ((ItemViewHolder) holder).mac_tv.setText("资产编码:"+mPoint.getAkey());
        ((ItemViewHolder) holder).area_tv.setText("盘点区域:"+mPoint.getArea());
        ((ItemViewHolder) holder).type_tv.setText("资产类别:"+mPoint.getPnamed()+"-"+mPoint.getApnamed());
        ((ItemViewHolder) holder).create_time_tv.setText("入库时间:"+mPoint.getAddTime());
        ((ItemViewHolder) holder).overtime_tv.setText("过期时间:"+mPoint.getOverTime());
        ((ItemViewHolder) holder).checkuser_tv.setText("盘点人:"+mPoint.getCheckUser());
        ((ItemViewHolder) holder).checktime_tv.setText("盘点时间:"+mPoint.getCheckTime());

        ((ItemViewHolder) holder).state_tv.setVisibility(View.VISIBLE);
        ((ItemViewHolder) holder).state_tv.setText(mPoint.getIfFinishName());
        String state="";
        switch (mPoint.getState()){
            case 0:
                state="正常";
                break;
            case 1:
                state="报警";
                break;
            case 2:
                state="维护中";
                break;
        }
        ((ItemViewHolder) holder).state2_tv.setText("状态:"+state);
        ((ItemViewHolder) holder).memo.setText("备注:"+mPoint.getMemo());
        ((ItemViewHolder) holder).commit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,DealAssetActivity.class);
                i.putExtra("order",mPoint);
                mContext.startActivity(i);
            }
        });
        ((ItemViewHolder) holder).update_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,UpdateAssetDataActivity.class);
                i.putExtra("order",mPoint);
                mContext.startActivity(i);
            }
        });
        ((ItemViewHolder) holder).upload_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,UploadAssetProblemActivity.class);
                i.putExtra("akey",mPoint.getAkey());
                i.putExtra("areaId",mPoint.getAreaId());
                mContext.startActivity(i);
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
            mOnItemClickListener.onItemClick(v, (AssetByCkeyEntity) v.getTag());
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
        @Bind(R.id.create_time_tv)
        TextView create_time_tv;
        @Bind(R.id.overtime_tv)
        TextView overtime_tv;
        @Bind(R.id.state2_tv)
        TextView state2_tv;
        @Bind(R.id.commit_tv)
        TextView commit_tv;
        @Bind(R.id.update_tv)
        TextView update_tv;
        @Bind(R.id.upload_tv)
        TextView upload_tv;
        @Bind(R.id.memo_tv)
        TextView memo;
        @Bind(R.id.rela)
        RelativeLayout rela;
        @Bind(R.id.checkbox)
        CheckBox checkbox;
        @Bind(R.id.checkuser_tv)
        TextView checkuser_tv;
        @Bind(R.id.checktime_tv)
        TextView checktime_tv;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
