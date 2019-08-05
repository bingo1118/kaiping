package com.smart.cloud.fire.activity.Inspection.InspectionMain;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart.cloud.fire.activity.Camera.CameraDevActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNFCItemActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNormalItemActivity;
import com.smart.cloud.fire.activity.Inspection.InspectionMap.InspectionMapActivity;
import com.smart.cloud.fire.activity.Inspection.NoticeListActivity.NoticeListActivity;
import com.smart.cloud.fire.activity.Inspection.PointList.PointListActivity;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadInspectionInfoActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadProblemActivity;
import com.smart.cloud.fire.activity.Setting.MyZoomActivity;
import com.smart.cloud.fire.base.ui.MvpV4Fragment;
import com.smart.cloud.fire.mvp.main.Main3Activity;
import com.smart.cloud.fire.mvp.main.presenter.MainPresenter;
import com.smart.cloud.fire.mvp.main.view.MainView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspMainFragment extends Fragment {

    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inspection_main, container, false);
        ButterKnife.bind(this, view);
        mContext=getActivity();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.insp_add_normal_ib,R.id.insp_add_nfc_ib,R.id.insp_task_ib
            ,R.id.insp_points_ib,R.id.insp_map_ib,R.id.insp_upload_problem_ib
            ,R.id.insp_quickly_ib,R.id.insp_fire,R.id.insp_camera
            ,R.id.my_image,R.id.insp_notice_ib})
    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.my_image:
                intent = new Intent(mContext, MyZoomActivity.class);
                break;
            case R.id.insp_add_normal_ib:
                intent=new Intent(mContext, AddInspectionNormalItemActivity.class);
                break;
            case R.id.insp_add_nfc_ib:
                intent=new Intent(mContext, AddInspectionNFCItemActivity.class);
                break;
            case R.id.insp_task_ib:
                intent=new Intent(mContext, TaskListActivity.class);
                break;
            case R.id.insp_points_ib:
                intent=new Intent(mContext, PointListActivity.class);
                break;
            case R.id.insp_map_ib:
                intent=new Intent(mContext, InspectionMapActivity.class);
                break;
            case R.id.insp_upload_problem_ib:
                intent=new Intent(mContext, UploadProblemActivity.class);
                break;
            case R.id.insp_quickly_ib:
                intent=new Intent(mContext, UploadInspectionInfoActivity.class);
                break;
            case R.id.insp_fire:
                intent=new Intent(mContext, Main3Activity.class);
                break;
            case R.id.insp_camera:
                intent=new Intent(mContext, CameraDevActivity.class);
                break;
            case R.id.insp_notice_ib:
                intent=new Intent(mContext, NoticeListActivity.class);
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }

}
