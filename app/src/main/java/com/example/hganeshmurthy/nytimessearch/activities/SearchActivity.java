package com.example.hganeshmurthy.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;

import com.example.hganeshmurthy.nytimessearch.ArticleRecyclerViewAdapter;
import com.example.hganeshmurthy.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.hganeshmurthy.nytimessearch.NYTArticle;
import com.example.hganeshmurthy.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.tbSearch) Toolbar tbSearch;
    @Bind(R.id.rvArticles) RecyclerView rvArticles;
    @Bind(R.id.searchView) SearchView searchView;
    public static final String CLIENT_ID = "62ed57f005883134ce24def61b3fe8dd:1:74339472";
    ArrayList<NYTArticle> allArticles;
    ArticleRecyclerViewAdapter rcAdapter;
    public static int noOfElements = 0;
    public static int noOfPages = 0;
    private final int REQUEST_CODE_DISPLAY = 30;

    String date;
    String order;
    Boolean arts;
    Boolean fashion;
    Boolean sports;


    private StaggeredGridLayoutManager gaggeredGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(tbSearch);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rvArticles);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        allArticles = new ArrayList<>();
        getArticleData();
        rcAdapter = new ArticleRecyclerViewAdapter(SearchActivity.this,allArticles);
        recyclerView.setAdapter(rcAdapter);



         recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gaggeredGridLayoutManager) {
           @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {

        getArticleData();
    }

    public void getArticleData()
    {
        int curSize = 0;
        int artSize =0;
        if(rcAdapter != null &&  allArticles.size()>0) {
            curSize = rcAdapter.getItemCount();
            artSize = allArticles.size();
            //allArticles.clear();
            //rcAdapter.notifyItemRangeChanged(curSize,artSize);
        }

        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json?api-key="+CLIENT_ID+"&page="+noOfPages;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject mainJson = response.getJSONObject("response");
                    JSONArray nYTArticles = mainJson.getJSONArray("docs");
                    int flag;
                    for (int i = 0; i < nYTArticles.length(); i++) {
                        flag = 0;
                        noOfElements++;
                        if (noOfElements % 10 == 0)
                            noOfPages++;
                        JSONObject articleJson = nYTArticles.getJSONObject(i);
                        NYTArticle article = new NYTArticle();
                        if (articleJson.getString("web_url") != null) {
                            String web_url = articleJson.getString("web_url");
                            article.setWeb_url(web_url);
                        } else {
                            flag = 1;
                        }
                        if (articleJson.getJSONObject("headline").getString("main") != null) {

                            String headline = (articleJson.getJSONObject("headline").getString("main")).replace("&amp;", "&");
                            article.setPrint_headline(headline);
                        } else {
                            flag = 1;
                        }

                        if (articleJson.getJSONArray("multimedia").length() != 0) {
                            JSONArray mutimediaArray = articleJson.getJSONArray("multimedia");
                            for (int j = 0; j < mutimediaArray.length(); j++) {
                                JSONObject multimediaJson = mutimediaArray.getJSONObject(j);
                                if (multimediaJson.getInt("width") == 600) {
                                    article.setMultimedia_url(multimediaJson.getString("url"));
                                    break;
                                } else if (multimediaJson.getInt("width") == 190) {
                                    article.setMultimedia_url(multimediaJson.getString("url"));
                                } else {
                                    article.setMultimedia_url(multimediaJson.getString("url"));
                                }
                            }
                        } else {
                            flag = 1;
                        }
                        if (flag == 0) {
                            allArticles.add(article);
                            rcAdapter.notifyItemChanged(allArticles.size());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }


    public void filterArticles(View view)
    {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("date",date);
        intent.putExtra("order", order);
        intent.putExtra("arts",arts);
        intent.putExtra("fashion",fashion);
        intent.putExtra("sports",sports);
        startActivityForResult(intent, REQUEST_CODE_DISPLAY);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DISPLAY) {
             date = data.getExtras().getString("date");
             order = data.getExtras().getString("order");
             arts = data.getExtras().getBoolean("arts");
             fashion = data.getExtras().getBoolean("fashion");
             sports = data.getExtras().getBoolean("sports");

        }
    }
        public void onArticleSearch(View view)
    {
        String query = "android";
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json?api-key="+CLIENT_ID+"&q"+query;
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject mainJson = response.getJSONObject("response");
                    JSONArray nYTArticles = mainJson.getJSONArray("docs");
                    for (int i = 0; i < nYTArticles.length(); i++) {

                        JSONObject articleJson = nYTArticles.getJSONObject(i);
                        NYTArticle article = new NYTArticle();
                        if(articleJson.getString("web_url") != null) {
                            article.setWeb_url(articleJson.getString("web_url") );
                        }
                        if(articleJson.getJSONObject("headline").getString("main") != null) {
                            article.setPrint_headline(articleJson.getJSONObject("headline").getString("main"));
                        }

                        if(articleJson.getJSONArray("multimedia")!= null) {
                            JSONArray mutimediaArray = articleJson.getJSONArray("multimedia");
                            for (int j = 0; j < mutimediaArray.length(); j++) {
                                JSONObject multimediaJson = mutimediaArray.getJSONObject(j);
                                if(multimediaJson.getString("width") == "600") {
                                    article.setMultimedia_url(multimediaJson.getString("url"));
                                }
                                else if(multimediaJson.getString("width") == "190") {
                                    article.setMultimedia_url(multimediaJson.getString("url"));
                                }
                                else {
                                    article.setMultimedia_url(multimediaJson.getString("url"));
                                }
                            }
                        }
                        allArticles.add(article);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
        rcAdapter.notifyDataSetChanged();

    }

}
