package com.paa0609.seproject;

public class Article {

    String title;
    String busNum;
    String object;
    String articleTime;


    public Article(String title, String busNum, String object, String articleTime) {
        this.title = title;
        this.busNum = busNum;
        this.object = object;
        this.articleTime = articleTime;

    }

    public String getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }

    public Article(String title, String busNum, String object) {
        this.title = title;
        this.busNum = busNum;
        this.object = object;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusNum() { return busNum; }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
