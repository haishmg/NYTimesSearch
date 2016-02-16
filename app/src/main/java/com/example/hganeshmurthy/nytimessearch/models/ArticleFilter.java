package com.example.hganeshmurthy.nytimessearch.models;

import org.parceler.Parcel;

/**
 * Created by hganeshmurthy on 2/15/16.
 */
@Parcel
public class ArticleFilter {

    String date;
    String order;
    Boolean arts;
    Boolean fashion;
    Boolean sports;

    public ArticleFilter ()
    {

    }
    public ArticleFilter (String date, String order, Boolean arts, Boolean fashion, Boolean sports)
    {
        this.date = date;
        this.order = order;
        this.arts = arts;
        this.fashion = fashion;
        this.sports = sports;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Boolean getArts() {
        return arts;
    }

    public void setArts(Boolean arts) {
        this.arts = arts;
    }

    public Boolean getFashion() {
        return fashion;
    }

    public void setFashion(Boolean fashion) {
        this.fashion = fashion;
    }

    public Boolean getSports() {
        return sports;
    }

    public void setSports(Boolean sports) {
        this.sports = sports;
    }

}
