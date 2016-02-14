package com.example.hganeshmurthy.nytimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.hganeshmurthy.nytimessearch.NYTArticle;
import com.example.hganeshmurthy.nytimessearch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DisplayArticle extends AppCompatActivity {
    @Bind (R.id.wvArticle)
    WebView wvArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
        ButterKnife.bind(this);

        NYTArticle article = (NYTArticle)getIntent().getSerializableExtra("article");
        //wvArticle.loadUrl(url);
        wvArticle.setWebViewClient( new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvArticle.loadUrl(article.getWeb_url());
    }
}
