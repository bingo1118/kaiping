package com.smart.cloud.fire.activity.Inspection.AddInspectionItem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.GetLocationActivity;
import com.smart.cloud.fire.activity.AddNFC.NFCDeviceType;
import com.smart.cloud.fire.activity.AddNFC.NFCInfo;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.Point;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.SelectPhotoView;
import com.smart.cloud.fire.view.XCDropDownListView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddPointActivity extends MvpActivity<AddInspectionItemPresenter> implements AddInspectionItemView {

    @Bind(R.id.add_fire_name)
    EditText addFireName;//设备名称。。
    @Bind(R.id.add_fire_lat)
    EditText addFireLat;//经度。。
    @Bind(R.id.add_fire_lon)
    EditText addFireLon;//纬度。。
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;//设备地址。。
    @Bind(R.id.add_fire_zjq)
    XCDropDownListView addFireZjq;//选择区域。。
    @Bind(R.id.add_fire_type)
    Spinner addFireType;//选择类型。。
    @Bind(R.id.add_fire_dev_btn)
    TextView addFireDevBtn;//添加设备按钮。。

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;//加载进度。。
    @Bind(R.id.add_camera_name)
    EditText addCameraName;

    private Context mContext;
    private int privilege;
    private String userID;
    private ShopType mShopType;
    private Point mPoint;
    private Area mArea;
    private NFCDeviceType nfcDeviceType;//@@8.16
    private String areaId = "";

    int protectionLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);

        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        init();
    }

    private void init() {
        String[] mItems = {"市级", "省级", "国级"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addFireType.setAdapter(adapter);
        addFireType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                protectionLevel = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });

        addFireZjq.setEditTextHint("区域");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addFire();
            }
        });
    }


    /**
     * 添加设备，提交设备信息。。
     */
    private void addFire() {
        if (mArea != null) {
            areaId = mArea.getAreaId();
        }
        String longitude = addFireLon.getText().toString().trim();
        String latitude = addFireLat.getText().toString().trim();
        String name = addFireName.getText().toString().trim();
        String address = addFireAddress.getText().toString().trim();

        if (longitude.length() == 0 || latitude.length() == 0) {
            toast("请获取经纬度");
            return;
        }
        if (name.length() == 0 || name.length() == 0) {
            toast("请填写名称");
            return;
        }

        if (areaId == null || areaId.length() == 0) {
            toast("请填选择区域");
            return;
        }


        showLoading();

        mvpPresenter.addPoint(areaId,name,address,longitude,latitude,protectionLevel+"");
    }

    @Override
    protected AddInspectionItemPresenter createPresenter() {
        AddInspectionItemPresenter addNFCPresenter = new AddInspectionItemPresenter(this);
        return addNFCPresenter;
    }


    @Override
    public void onDestroy() {
        mvpPresenter.stopLocation();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        mvpPresenter.initLocation();
        super.onStart();
    }

    @OnClick({R.id.location_image, R.id.add_fire_zjq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_image:
                Intent intent = new Intent(mContext, GetLocationActivity.class);
                startActivityForResult(intent, 1);//@@6.20
                break;
            case R.id.add_fire_zjq:
                if (addFireZjq.ifShow()) {
                    addFireZjq.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    addFireZjq.setClickable(false);
                    addFireZjq.showLoading();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void getLocationData(BDLocation location) {
        addFireLon.setText(location.getLongitude() + "");
        addFireAddress.setText(location.getAddrStr());
        addFireLat.setText(location.getLatitude() + "");
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
    }

    @Override
    public void getDataSuccess(Smoke smoke) {

    }

    @Override
    public void getPoints(ArrayList<Object> shopTypes) {

    }

    @Override
    public void getPointsFail(String msg) {

    }

    @Override
    public void getShopType(ArrayList<Object> shopTypes) {

    }

    @Override
    public void getShopTypeFail(String msg) {

    }

    @Override
    public void getNFCDeviceType(ArrayList<Object> deviceTypes) {

    }

    @Override
    public void getNFCDeviceTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireType.setClickable(true);
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        addFireZjq.setItemsData(shopTypes, mvpPresenter);
        addFireZjq.showPopWindow();
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://@@6.20
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getBundleExtra("data");
                    addFireLat.setText(String.format("%.8f", bundle.getDouble("lat")));
                    addFireLon.setText(String.format("%.8f", bundle.getDouble("lon")));
                    addFireAddress.setText(bundle.getString("address"));
                }
                break;
        }

    }

    //@@10.12存储文件到sd卡
    public void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);//检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));//100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    //@@10.12压缩图片尺寸
    public Bitmap compressBySize(String pathName, int targetWidth,
                                 int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);// 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }//设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    @Override
    public void addSmokeResult(String msg, int errorCode) {
        T.showShort(mContext, msg);
        if (errorCode == 0) {
            mShopType = null;
            mArea = null;
            clearText();
            addFireZjq.addFinish();
        }
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
    }

    @Override
    public void getChoicePoint(Point point) {
        mPoint = point;
    }

    @Override
    public void getChoiceNFCDeviceType(NFCDeviceType nfcDeviceType) {
        this.nfcDeviceType = nfcDeviceType;
    }


    /**
     * 清空其他编辑框内容。。
     */
    private void clearText() {
        addFireLon.setText("");
        addFireLat.setText("");
        addFireAddress.setText("");
        addFireName.setText("");
        addFireZjq.setEditTextData("");//@@10.19
        addCameraName.setText("");
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}

