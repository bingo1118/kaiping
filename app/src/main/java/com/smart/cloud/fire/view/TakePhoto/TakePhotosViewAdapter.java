package com.smart.cloud.fire.view.TakePhoto;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fire.cloud.smart.com.smartcloudfire.R;

import com.bumptech.glide.Glide;
import com.smart.cloud.fire.activity.Inspection.InspHistory.InspHistoryAdapter;
import com.smart.cloud.fire.activity.NFCDev.NFCImageShowActivity;

import java.util.List;

public class TakePhotosViewAdapter extends RecyclerView.Adapter<TakePhotosViewAdapter.ViewHolder> {

    private List<Photo> mList;
    private Context mContext;


    private boolean isShowAdd=true;

    public  TakePhotosViewAdapter (Context context,List <Photo> list){
        mContext=context;
        mList = list;
    }

    public void setmList(List<Photo> mList,boolean isShowAdd) {
        this.mList = mList;
        this.isShowAdd=isShowAdd;
        notifyDataSetChanged();
    }

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    OnClickListener mOnClickListener;
    interface OnClickListener{
        public void onItemClick(Photo mPhoto, int position);
    };

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        if(position == mList.size()){
            holder.photo_iv.setImageResource(R.drawable.add);
            holder.photo_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnClickListener!=null){
                        mOnClickListener.onItemClick(null,position);
                    }
                }
            });
        }else{
            final Photo photo = mList.get(position);
            Glide.with(mContext)
                    .load(photo.getPath())
                    .placeholder(R.drawable.photo_place)
                    .into(holder.photo_iv);
            holder.photo_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isShowAdd){
                        Intent intent = new Intent(mContext, NFCImageShowActivity.class);
                        intent.putExtra("path",photo.getPath());
                        mContext.startActivity(intent);
                    }else{
                        if(mOnClickListener!=null){
                            mOnClickListener.onItemClick(photo,position);
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount(){
        if(isShowAdd){
            return mList.size()+1;
        }else {
            return mList.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photo_iv;

        public ViewHolder (View view)
        {
            super(view);
            photo_iv = (ImageView) view.findViewById(R.id.photo_iv);
        }

    }
}
