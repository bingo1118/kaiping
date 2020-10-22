package com.smart.cloud.fire.activity.AssetManage.TagAlarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.order.OrderInfoDetail.OrderInfoAdapter;
import com.smart.cloud.fire.utils.MusicManger;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.view.MyImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class TagAlarmPushActivity extends Activity {


    @Bind(R.id.asset_photo_iv)
    ImageView asset_photo_iv;
    @Bind(R.id.asset_name_tv)
    TextView asset_name_tv;
    @Bind(R.id.asset_alarmtype_tv)
    TextView asset_alarmtype_tv;
    @Bind(R.id.asset_id_tv)
    TextView asset_id_tv;
    @Bind(R.id.address_tv)
    TextView address_tv;
    @Bind(R.id.time_tv)
    TextView time_tv;
    @Bind(R.id.location_tv)
    TextView location_tv;
    @Bind(R.id.maneger_tv)
    TextView maneger_tv;
    @Bind(R.id.phone_tv)
    TextView phone_tv;
    @Bind(R.id.alarm_fk_img)
    MyImageView alarmFkImg;


    private Context mContext;
    private TagAlarmInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title
        //在锁屏状态下弹出。。
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tag_alarm_push);

        ButterKnife.bind(this);
        mContext = this;
        mInfo = (TagAlarmInfo) getIntent().getExtras().getSerializable("info");

        init();
        regFilter();
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("CLOSE_ALARM_ACTIVITY");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CLOSE_ALARM_ACTIVITY")) {
                finish();
            }
        }
    };

    private void init() {
        if (mInfo != null) {
            asset_name_tv.setText(mInfo.getNamed());
            asset_alarmtype_tv.setText("告警类型:" + mInfo.getAlarmName());
            asset_id_tv.setText("资产编号:"+mInfo.getMac());
            address_tv.setText("地         址:"+mInfo.getAddress());
            time_tv.setText("告警时间:"+mInfo.getAlarmTime());
            location_tv.setText("标签位置:"+mInfo.getTagName());
            maneger_tv.setText("负  责  人:"+mInfo.getPrincapal());
            phone_tv.setText("联系方式:"+mInfo.getPhone());
            String url= "http://139.199.58.208//assetImage//"+ mInfo.getPhoto();
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.photo_normal)
                    .error(R.drawable.photo_normal)
                    .thumbnail((float)0.0001)
                    .thumbnail(0.00001f)
                    .into(asset_photo_iv);
        }
        MusicManger.getInstance().playTagAlarmMusic(mContext);
        alarmInit();
    }

    private void alarmInit() {
        //imageview动画设置。。
        final AnimationDrawable anim = (AnimationDrawable) alarmFkImg
                .getBackground();
        ViewTreeObserver.OnPreDrawListener opdl = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                anim.start();
                return true;
            }
        };
        alarmFkImg.getViewTreeObserver().addOnPreDrawListener(opdl);
    }


    @OnClick({R.id.more_tv, R.id.close_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_tv:
                Intent intent=new Intent(mContext,TagAlarmListActivity.class);
                startActivity(intent);
                break;
            case R.id.close_tv:
                finish();
                break;
            default:
                break;
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        acquireWakeLock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManger.getInstance().stop();
        releaseWakeLock();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
    }

    private PowerManager.WakeLock mWakelock;

    private void acquireWakeLock() {
        if (mWakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
            mWakelock.acquire();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        ButterKnife.bind(this);
        mContext = this;
        mInfo = (TagAlarmInfo) intent.getExtras().getSerializable("info");

        init();
        regFilter();
        super.onNewIntent(intent);
    }
}
