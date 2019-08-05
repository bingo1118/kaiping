package com.smart.cloud.fire.activity.Inspection.NoticeListActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class NoticeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<NoticeEntity> itemsList;


    public NoticeListAdapter(Context mContext, List<NoticeEntity> electricList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.itemsList = electricList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.insp_notice_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        NoticeListAdapter.ItemViewHolder viewHolder = new NoticeListAdapter.ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        NoticeEntity noticeEntity=itemsList.get(position);
        String grade=noticeEntity.getNlevel();
//        ((ItemViewHolder) holder).grade_tv.setText(noticeEntity.getNlevelName());
        switch (grade){
            case "0":
                ((ItemViewHolder) holder).grade_tv.setBackgroundColor(mContext.getResources().getColor(R.color.playback_timebar_color));
                break;
            case "1":
                ((ItemViewHolder) holder).grade_tv.setBackgroundColor(mContext.getResources().getColor(R.color.action_now_color));
                break;
            case "2":
                ((ItemViewHolder) holder).grade_tv.setBackgroundColor(mContext.getResources().getColor(R.color.daohang_ed_bg_press));
                break;
        }
        ((ItemViewHolder) holder).time_tv.setText(noticeEntity.getPublishtime());
        ((ItemViewHolder) holder).title_tv.setText(noticeEntity.getTitle());
        ((ItemViewHolder) holder).content_text.setText(noticeEntity.getContent());
        if(noticeEntity.getIsback().equals("0")){
            ((ItemViewHolder) holder).detail_tv.setText("未读");
        }else{
            ((ItemViewHolder) holder).detail_tv.setText("已读");
        }
        ((ItemViewHolder) holder).item_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,NoticeDetailActivity.class);
                intent.putExtra("notice",noticeEntity);
                mContext.startActivity(intent);
                itemsList.get(position).setIsback("1");
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(itemsList!=null){
            return itemsList.size();
        }else{
            return 0;
        }
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.grade_tv)
        TextView grade_tv;
        @Bind(R.id.time_tv)
        TextView time_tv;
        @Bind(R.id.title_tv)
        TextView title_tv;
        @Bind(R.id.content_text)
        TextView content_text;
        @Bind(R.id.detail_tv)
        TextView detail_tv;
        @Bind(R.id.item_rela)
        RelativeLayout item_rela;


        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
