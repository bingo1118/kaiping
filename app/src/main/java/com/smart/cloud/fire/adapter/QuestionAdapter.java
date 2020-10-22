package com.smart.cloud.fire.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smart.cloud.fire.global.Question;
import com.smart.cloud.fire.utils.JsonUtils;
import com.smart.cloud.fire.view.TakePhoto.Photo;
import com.smart.cloud.fire.view.TakePhoto.TakePhotosView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<Question> list;

    public QuestionAdapter(Context mContext, List<Question> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.question_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    String pathtemp;
    String f;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question question=list.get(position);
        ((MyViewHolder) holder).quession_text.setText(question.getQdetail());
        if(question.isFirstItem()){
            ((MyViewHolder) holder).type_tv.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).type_tv.setText(question.getQuestionType());
        }else{
            ((MyViewHolder) holder).type_tv.setVisibility(View.GONE);
        }
        ((MyViewHolder) holder).question_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.yes_rb:
                        list.get(position).setAnswer(0);
                        break;
                    case R.id.no_rb:
                        list.get(position).setAnswer(1);
                        break;
                }
            }
        });
        ((MyViewHolder) holder).yes_rb.setChecked(true);//默认选择是

        ((MyViewHolder) holder).take_photo_view.setmList(question.getPhotos());
        ((MyViewHolder) holder).take_photo_view.setmOnClickListener(new TakePhotosView.OnClickListener() {
            @Override
            public void onItemClick() {
                if(mOnClickListener!=null){
                    mOnClickListener.onItemClick(position);
                }
            }
        });
        ((MyViewHolder) holder).remark_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    list.get(position).setRemark(((MyViewHolder) holder).remark_et.getText().toString());
                }
            }
        });
    }

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public OnClickListener mOnClickListener;
    public interface OnClickListener{
        public void onItemClick(int position);
    };


    public String getAnwserJson(){
        List<Map<String,String>> maps=new ArrayList<>();
        Gson gson=new Gson();

        for (Question q:list) {
            if(q.getAnswer()>2) return null;
            Map<String,String> map=new HashMap<>();
            if(q.getQjudge().equals(q.getAnswer()+"")){
                map.put(q.getQid()+"","0");//合格
            }else{
                map.put(q.getQid()+"","1");//不合格
            }

            maps.add(map);
        }
        return gson.toJson(maps);
    }

    class QuestionItem{
        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        String remark;
        String result;
        String images;
        String qid;
    }


    public String getAnwserJsonNew(){
        List<QuestionItem> maps=new ArrayList<>();
        Gson gson=new Gson();

        for (Question q:list) {
            QuestionItem questionItem=new QuestionItem();
            questionItem.setQid(q.getQid()+"");
            if(q.getQjudge().equals(q.getAnswer()+"")){
                questionItem.setResult("0");//合格
            }else{
                questionItem.setResult("1");//不合格
            }
            questionItem.setRemark(q.getRemark()+"");
            List<Photo> photos = q.getPhotos();
            String photoString="";
            for(int i=0;i<photos.size();i++){
                photoString+=(photos.get(i).getName()+".jpg");
                if(i!=(photos.size()-1)){
                    photoString+="#";
                }
            }
            questionItem.setImages(photoString);
            maps.add(questionItem);
        }
        return gson.toJson(maps);
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }else{
            return list.size();
        }
    }


    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.quession_text)
        TextView quession_text;
        @Bind(R.id.yes_rb)
        RadioButton yes_rb;
        @Bind(R.id.no_rb)
        RadioButton no_rb;
        @Bind(R.id.question_rg)
        RadioGroup question_rg;
        @Bind(R.id.type_tv)
        TextView type_tv;
        @Bind(R.id.take_photo_view)
        TakePhotosView take_photo_view;
        @Bind(R.id.remark_et)
        EditText remark_et;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
