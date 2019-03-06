package com.paa0609.seproject;

import java.io.Serializable;

public class ArticleObject implements Serializable {

    private static final long serialVersionUID = 6631779405103795L;

    private String ID;
    private String busNum;
    private String Object;
    private String phoneNum;
    private String contents;
    private String postTime;
    private String title;
    private boolean isComplete = false;

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    private boolean myArticle;

    public boolean isMyArticle() {
        return myArticle;
    }

    public void setMyArticle(boolean myArticle) {
        this.myArticle = myArticle;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
