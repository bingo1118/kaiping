package com.smart.cloud.fire.activity.Inspection.InspectionMap;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.MyOverlayManager;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.base.ui.MvpV4Fragment;
import com.smart.cloud.fire.global.NFCInfoEntity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.BingoDropDowmListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspMapFragment extends MvpFragment<InspectionMapPresenter> implements InspectionMapView{

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.area_dropdowmlistview)
    BingoDropDowmListView area_dropdowmlistview;

    private String userID;
    private BaiduMap mBaiduMap;
    private InspectionMapPresenter mPresenter;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inspection_map, container, false);
        ButterKnife.bind(this, view);
        mBaiduMap = mMapView.getMap();// 获得MapView
        mContext=getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        area_dropdowmlistview.setEditTextHint("区域");
        area_dropdowmlistview.initData("");
        area_dropdowmlistview.setmOnSelectedItem(new BingoDropDowmListView.OnSelectedItem() {
            @Override
            public void onSelectedItem(String selectrdItemId) {
                mPresenter.getItemInfoByAreaId(userID,selectrdItemId);
            }
        });
        mPresenter.getAllItems(userID,"");
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected InspectionMapPresenter createPresenter() {
        if(mPresenter==null){
            mPresenter=new InspectionMapPresenter(this);
        }
        return mPresenter;
    }



    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList, int sum, int pass, int checked) {

    }

    private MyOverlayManager mMyOverlayManager;
    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList) {
        mBaiduMap.clear();
        List<BitmapDescriptor> viewList =  initMark();
        if(mMyOverlayManager==null){
            mMyOverlayManager = new MyOverlayManager();
        }
        mMyOverlayManager.initInspection(mContext,mBaiduMap,pointList,viewList);
        mMyOverlayManager.removeFromMap();
        mBaiduMap.setOnMarkerClickListener(mMyOverlayManager);
        mMyOverlayManager.addToMap();
        mMyOverlayManager.zoomToSpan();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMyOverlayManager.zoomToSpan();
            }
        });
    }

    /**
     * 初始化各种设备的标记图标。。
     * @return
     */
    private List<BitmapDescriptor> initMark(){
        View viewA = LayoutInflater.from(mContext).inflate(
                R.layout.image_mark, null);
        BitmapDescriptor bdA = BitmapDescriptorFactory
                .fromView(viewA);
        List<BitmapDescriptor> listView = new ArrayList<>();
        listView.add(bdA);
        return listView;
    }

    @Override
    public void getDataFail(String msg) {

    }

//    @OnClick({  R.id.area_dropdowmlistview})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.area_dropdowmlistview:
//                if (area_dropdowmlistview.ifShow()) {
//                    area_dropdowmlistview.closePopWindow();
//                } else {
////                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
////                    area_dropdowmlistview.setClickable(false);
//                    area_dropdowmlistview.showPopWindow();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public String getFragmentName() {
        return "INSP_MAP";
    }


    public void clearState(){
        area_dropdowmlistview.closePopWindow();
    }
}
