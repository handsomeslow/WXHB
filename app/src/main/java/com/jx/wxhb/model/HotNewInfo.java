package com.jx.wxhb.model;

import io.realm.RealmObject;

/**
 * Created by 徐俊 on 2017/2/18.
 */

public class HotNewInfo extends RealmObject {
    private String title;

    private String url;
    // 公众号
    private String official;

    private double score;

    // 10万+
    private String readcount;

    private String likecount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOfficial() {
        return official;
    }

    public void setOfficial(String official) {
        this.official = official;
    }

    public String getReadcount() {
        return readcount;
    }

    public void setReadcount(String readcount) {
        this.readcount = readcount;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
