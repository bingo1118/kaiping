package com.smart.cloud.fire.global;

import com.smart.cloud.fire.view.TakePhoto.Photo;

import java.util.ArrayList;
import java.util.List;

public class Question {

    String qdetail;
    int qid;
    int answer=3;
    String questionType;
    boolean isFirstItem;
    String qjudge;//合格答案
    String remark;
    List<Photo> photos=new ArrayList<>();

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }



    public String getQjudge() {
        return qjudge;
    }

    public void setQjudge(String qjudge) {
        this.qjudge = qjudge;
    }



    public boolean isFirstItem() {
        return isFirstItem;
    }

    public void setFirstItem(boolean firstItem) {
        isFirstItem = firstItem;
    }


    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }


    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getQdetail() {
        return qdetail;
    }

    public void setQdetail(String qdetail) {
        this.qdetail = qdetail;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }


}
