package com.example.hganeshmurthy.nytimessearch;

import org.parceler.Parcel;

/**
 * Created by hganeshmurthy on 2/10/16.
 */
@Parcel
public class NYTArticle {
    String web_url;
    String snippet;
    String print_headline;
    String multimedia_url;

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getPrint_headline() {
        return print_headline;
    }

    public void setPrint_headline(String print_headline) {
        this.print_headline = print_headline;
    }

    public String getMultimedia_url() {
        return multimedia_url;
    }

    public void setMultimedia_url(String multimedia_url) {
        this.multimedia_url = multimedia_url;
    }
}
