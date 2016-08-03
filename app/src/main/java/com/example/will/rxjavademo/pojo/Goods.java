package com.example.will.rxjavademo.pojo;

import java.util.List;

/**
 * Created by will on 16/8/2.
 */

public class Goods {
    private String badge;

    private int buy_unit;

    private long created_utc;

    private int gid;

    private List<String> image;

    private int issue_id;

    private int left_unit;

    private int price;

    private String summary;

    private String title;

    public String getBadge() {
        return this.badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getBuy_unit() {
        return this.buy_unit;
    }

    public void setBuy_unit(int buy_unit) {
        this.buy_unit = buy_unit;
    }


    public int getGid() {
        return this.gid;
    }

    public long getCreated_utc() {
        return this.created_utc;
    }

    public void setCreated_utc(long created_utc) {
        this.created_utc = created_utc;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public List<String> getImage() {
        return this.image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public int getIssue_id() {
        return this.issue_id;
    }

    public void setIssue_id(int issue_id) {
        this.issue_id = issue_id;
    }

    public int getLeft_unit() {
        return this.left_unit;
    }

    public void setLeft_unit(int left_unit) {
        this.left_unit = left_unit;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
