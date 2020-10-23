package com.smart.cloud.fire.view.TakePhoto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

public class TakePhotosView extends LinearLayout {

    Context mContext;
    List<Photo> mList;

    public TakePhotosView(Context context) {
        this(context,null,0);
    }

    public TakePhotosView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TakePhotosView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initView();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TakePhotosView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public OnClickListener mOnClickListener;
    public interface OnClickListener{
        public void onItemClick();
    };

    public void setmList(List<Photo> list,boolean isShowAdd) {
        this.mList = list;
        adapter.setmList(mList,isShowAdd);
    }

    RecyclerView recyclerView;
    TakePhotosViewAdapter adapter;


    private void initView() {
        mList=new ArrayList<>();

        View view = LayoutInflater.from(mContext).inflate(R.layout.take_photo_view,null);
        recyclerView=view.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TakePhotosViewAdapter(mContext,mList);
        recyclerView.setAdapter(adapter);
        adapter.setmOnClickListener(new TakePhotosViewAdapter.OnClickListener() {
            @Override
            public void onItemClick(Photo mPhoto, int position) {
                if(position==(mList.size())){
                    if(mOnClickListener!=null){
                        mOnClickListener.onItemClick();
                    }
                }else{
                    if(mPhoto==null){
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //进行图片-->bitmap-->uri转换
                    File file = new File(mPhoto.getPath());
                    Uri uri = Uri.fromFile(file);
                    intent.setDataAndType(uri,"image/*");
                    mContext.startActivity(intent);
                }
            }
        });
        addView(view);
    }
}
