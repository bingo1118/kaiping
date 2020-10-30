package com.smart.cloud.fire.activity.Inspection.InspHistory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smart.cloud.fire.global.Question;
import com.smart.cloud.fire.view.TakePhoto.Photo;
import com.smart.cloud.fire.view.TakePhoto.TakePhotosView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspHistoryItemQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<Question> list;
    private boolean isShowAdd;

    public InspHistoryItemQuestionAdapter(Context mContext, List<Question> list,boolean isShow) {
        this.mContext = mContext;
        this.list = list;
        this.isShowAdd=isShow;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.question_history_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    String pathtemp;
    String f;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question question=list.get(position);
        ((MyViewHolder) holder).quession_text.setText(question.getQdetail());
        if(question.getAnswer()==0){
            ((MyViewHolder) holder).question_rg.check(((MyViewHolder) holder).yes_rb.getId());
        }else{
            ((MyViewHolder) holder).question_rg.check(((MyViewHolder) holder).no_rb.getId());
        }
        ((MyViewHolder) holder).yes_rb.setClickable(false);
        ((MyViewHolder) holder).no_rb.setClickable(false);

        ((MyViewHolder) holder).remark_et.setText(question.getRemark());
        if(question.isFirstItem()){
            ((MyViewHolder) holder).type_tv.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).type_tv.setText(question.getQuestionType());
        }else{
            ((MyViewHolder) holder).type_tv.setVisibility(View.GONE);
        }
        if(question.getQjudge().equals(question.getAnswer()+"")){
            ((MyViewHolder) holder).line.setVisibility(View.GONE);
        }else{
            ((MyViewHolder) holder).line.setVisibility(View.VISIBLE);
            if(question.getPhotos().size()!=0){
                ((MyViewHolder) holder).take_photo_view.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).take_photo_view.setmList(question.getPhotos(),isShowAdd);
                ((MyViewHolder) holder).take_photo_view.setmOnClickListener(new TakePhotosView.OnClickListener() {
                    @Override
                    public void onItemClick() {
                        if(mOnClickListener!=null){
                            mOnClickListener.onItemClick(position);
                        }
                    }
                });
            }else{
                ((MyViewHolder) holder).take_photo_view.setVisibility(View.GONE);
            }

        }

        ((MyViewHolder) holder).remark_et.setEnabled(false);
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
            questionItem.setResult(q.getAnswer()+"");//合格
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
        @Bind(R.id.line)
        LinearLayout line;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

