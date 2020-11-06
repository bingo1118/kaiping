package com.smart.cloud.fire.utils.SignView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.smart.cloud.fire.utils.T;

import java.io.File;
import java.io.IOException;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * @author shuang
 * @date 2016/11/3
 */

public class SignatureActivity extends Activity {

    private Context mContext;
    private static final String TAG = SignatureActivity.class.getSimpleName();
    private GestureSignatureView mMSignature;
    boolean result=false;

    public static String signaturePath= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "signature.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_signature);
        mContext=this;
        mMSignature = (GestureSignatureView) findViewById(R.id.gsv_signature);

        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMSignature.clear();
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    result = mMSignature.save(signaturePath);
                    if(result){
                        T.showShort(mContext,"保存成功");
                        setResult(1);
                    }else{
                        T.showShort(mContext,"保存失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    finish();
                }
            }
        });
    }

}
