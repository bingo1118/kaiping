package com.smart.cloud.fire.activity.Inspection.InspHistory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.smart.cloud.fire.activity.NFCDev.NFCImageShowActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Question;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.view.TakePhoto.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspHIstoryItemActivity extends Activity {

    @Bind(R.id.question_recyclerview)
    RecyclerView question_recyclerview;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.time)
    TextView time_tv;
    @Bind(R.id.state)
    TextView state_tv;
    @Bind(R.id.worker_tv)
    TextView worker_tv;
    @Bind(R.id.memo_tv)
    TextView memo_tv;
    @Bind(R.id.location_photo_iv)
    ImageView location_photo_iv;
    @Bind(R.id.sign_photo_iv)
    ImageView sign_photo_iv;

    private Context mContext;
    private String uid;
    private String prid;

    List<Question> listQ;
    InspHistoryItemQuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insp_history_item);

        ButterKnife.bind(this);
        mContext = this;
        uid=getIntent().getStringExtra("uid");
        prid=getIntent().getStringExtra("prid");

        getNormalDevInfo();
    }

    private void getNormalDevInfo() {
        if(uid==null){
            return;
        }
        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url="";

        url= ConstantValues.SERVER_IP_NEW+"getQuestionResultList?uid="+uid+"&prid="+prid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String questionJson=response.getString("questionTypes");
                            name_tv.setText("名称:"+response.getString("deviceName"));
                            address_tv.setText("位置:"+response.getString("address"));
                            time_tv.setText("时间:"+response.getJSONObject("record").getString("checktime"));
                            state_tv.setText("总评:"+response.getString("deviceStateName"));
                            memo_tv.setText("备注:"+response.getString("memo"));
                            worker_tv.setText("巡检人:"+response.getJSONObject("record").getString("workerName"));

                            String location_img_path=ConstantValues.NFC_IMAGES+"cheakImg//"+response.getJSONObject("record").getString("imgs");
                            String sign_img_path=ConstantValues.NFC_IMAGES+"cheakImg//"+response.getJSONObject("record").getString("signature");

                            Glide.with(mContext)
                                    .load(location_img_path)
                                    .placeholder(R.drawable.photo_ok)
                                    .into(location_photo_iv);
                            location_photo_iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                        Intent intent = new Intent(mContext, NFCImageShowActivity.class);
                                        intent.putExtra("path",location_img_path);
                                        mContext.startActivity(intent);
                                }
                            });

                            Glide.with(mContext)
                                    .load(sign_img_path)
                                    .placeholder(R.drawable.photo_ok)
                                    .into(sign_photo_iv);
                            sign_photo_iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext, NFCImageShowActivity.class);
                                    intent.putExtra("path",sign_img_path);
                                    mContext.startActivity(intent);
                                }
                            });

                            dealwithQuestionJson(questionJson);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext,"网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void dealwithQuestionJson(String questionJson) {
        try {
            JSONArray jsonArray=new JSONArray(questionJson);
            if(jsonArray.length()!=0){
                listQ=new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject temp=jsonArray.getJSONObject(i);
                    String qtname=temp.getString("qtname");
                    JSONArray tempA=temp.getJSONArray("questions");
                    for(int j=0;j<tempA.length();j++){
                        Question question=new Question();
                        if(j==0){
                            question.setFirstItem(true);
                        }else{
                            question.setFirstItem(false);
                        }
                        question.setAnswer(tempA.getJSONObject(j).getInt("result"));
                        question.setQjudge(tempA.getJSONObject(j).getString("qjudge"));
                        question.setQuestionType(qtname);
                        question.setQdetail(tempA.getJSONObject(j).getString("qdetail"));
                        question.setQid(tempA.getJSONObject(j).getInt("qid"));
                        String photosString=tempA.getJSONObject(j).getString("images");
                        String[] photosList=photosString.split("#");
                        List<Photo> photos=new ArrayList<>();
                        for(String s:photosList){
                            if(s.length()>0){
                                photos.add(new Photo(s,ConstantValues.NFC_IMAGES+"cheakImg//"+s));
                            }
                        }
                        question.setRemark(tempA.getJSONObject(j).getString("remark"));
                        question.setPhotos(photos);
                        listQ.add(question);
                    }
                }
            }
            questionAdapter=new InspHistoryItemQuestionAdapter(mContext,listQ,false);
            question_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));

            question_recyclerview.setAdapter(questionAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
