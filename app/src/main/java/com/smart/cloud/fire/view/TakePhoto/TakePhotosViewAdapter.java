package com.smart.cloud.fire.view.TakePhoto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


    public void setmOnLongClickListener(OnLongClickListener mOnLongClickListener) {
        this.mOnLongClickListener = mOnLongClickListener;
    }
    OnLongClickListener mOnLongClickListener;
    interface OnLongClickListener{
        public void onItemLongClick(Photo mPhoto, int position);
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
                    .placeholder(R.drawable.photo_ok)
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
            holder.photo_iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!isShowAdd){
                        Intent intent = new Intent(mContext, NFCImageShowActivity.class);
                        intent.putExtra("path",photo.getPath());
                        mContext.startActivity(intent);
                    }else{
                        AlertDialog.Builder builder  = new AlertDialog.Builder(mContext);
                        builder.setTitle("确认" ) ;
                        builder.setMessage("确认删除该图片？" ) ;
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(mOnLongClickListener!=null){
                                    mOnLongClickListener.onItemLongClick(photo,position);
                                }
                            }
                        });
                        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                    return false;
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
