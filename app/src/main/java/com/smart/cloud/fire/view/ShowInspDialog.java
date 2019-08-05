package com.smart.cloud.fire.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.activity.NFCDev.NFCRecordBean;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.global.NFCInfoEntity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

public class ShowInspDialog {
    Activity context;
    @Bind(R.id.user_smoke_dialog_tv2)
    TextView userSmokeDialogTv2;
    @Bind(R.id.user_smoke_dialog_tv5)
    TextView userSmokeDialogTv5;//@@11.08
    @Bind(R.id.user_smoke_dialog_tv6)
    TextView userSmokeDialogTv6;//@@11.08
    @Bind(R.id.user_smoke_dialog_tv3)
    TextView userSmokeDialogTv3;

    @Bind(R.id.normal_lead_btn)
    Button normalLeadBtn;
    @Bind(R.id.user_smoke_dialog_tv4)
    TextView user_smoke_dialog_tv4;//@@8.7设备id

    private NFCInfoEntity smoke;
    private AlertDialog dialog;
    private View mView;
    private NFCRecordBean nfcsmoke;

    public ShowInspDialog(Activity context, NFCInfoEntity smoke) {
        this.context = context;
        this.smoke = smoke;
        View view = LayoutInflater.from(context).inflate(
                R.layout.map_insp_marker, null,false);
        ButterKnife.bind(this, view);
        showSmokeDialog(view);
    }


    public void showSmokeDialog(final View view) {
        RxView.clicks(normalLeadBtn).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Reference<Activity> reference = new WeakReference(context);
                        new InitBaiduNavi(reference.get(), smoke);
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                            ButterKnife.unbind(view);
                        }
                    }
                });

        userSmokeDialogTv2.setText(smoke.getDeviceName());
        userSmokeDialogTv3.setText(smoke.getAddress());
        user_smoke_dialog_tv4.setText("ID:"+smoke.getUid());//@@8.7
        userSmokeDialogTv5.setText("区域:"+smoke.getAreaName());//@@11.08
        userSmokeDialogTv6.setText("类型:"+ smoke.getDeviceTypeName());//@@11.08
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        if(!dialog.isShowing()){
            dialog.show();
        }
        dialog.setContentView(view);
    }
}

