package com.example.hganeshmurthy.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.example.hganeshmurthy.nytimessearch.ArticleRecyclerViewAdapter;
import com.example.hganeshmurthy.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.hganeshmurthy.nytimessearch.NYTArticle;
import com.example.hganeshmurthy.nytimessearch.R;
import com.example.hganeshmurthy.nytimessearch.models.ArticleFilter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.tbSearch)
    Toolbar tbSearch;
    @Bind(R.id.rvArticles)
    RecyclerView rvArticles;
    @Bind(R.id.searchView)
    android.widget.SearchView searchView;
    public static final String CLIENT_ID = "62ed57f005883134ce24def61b3fe8dd:1:74339472";
    ArrayList<NYTArticle> allArticles;
    ArticleRecyclerViewAdapter rcAdapter;
    public  int noOfElements = 0;
    public  int noOfPages = 1;
    private final int REQUEST_CODE_DISPLAY = 30;

    String date="";
    String order="";
    Boolean arts=false;
    Boolean fashion=false;
    Boolean sports=false;
    String searchQuery;
    Boolean filter=false;


    private StaggeredGridLayoutManager gaggeredGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(tbSearch);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvArticles);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        allArticles = new ArrayList<>();
        rcAdapter = new ArticleRecyclerViewAdapter(SearchActivity.this, allArticles);
        recyclerView.setAdapter(rcAdapter);
        searchView.setQueryHint("Search For Articles");


        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gaggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });


        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchQuery = query;
                if (allArticles != null)
                    allArticles.clear();
                if (rcAdapter != null)
                    rcAdapter.notifyDataSetChanged();
                noOfPages = 1;
                noOfElements = 0;
                getArticleData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {

        getArticleData();
    }

    public void getArticleData() {

        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=" + CLIENT_ID;

        String newsDesk = "&fq=news_desk:(";

        if (searchQuery != null)
            url = url + "&q=" + searchQuery;

        if (!order.equals(""))
            url = url + "&sort=" + order;


        if (arts == true)
            newsDesk = newsDesk + "\"Arts\" ";
        if (fashion == true)
            newsDesk = newsDesk + "\"Fashion\" ";
        if (sports == true)
            newsDesk = newsDesk + "\"Sports\" ";
        newsDesk = newsDesk + ")";

        if ((arts == true) || (fashion == true) || (sports == true))
            url = url + newsDesk;

        if (!date.equals("")) {
            String splitDate[] = date.split("/");
            if (splitDate.length > 0)
                url = url + "&end_date=" + splitDate[2] + splitDate[0] + splitDate[1];
        }


        url = url + "&page=" + noOfPages;

        if (isOnline() == true) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, null, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {

                        JSONObject mainJson = response.getJSONObject("response");
                        if (mainJson.has("docs") &&  mainJson.getJSONArray("docs").length() >0)
                             {
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
                                        if (multimediaJson.has("width")) {
                                            if (multimediaJson.getInt("width") == 600) {
                                                article.setMultimedia_url(multimediaJson.getString("url"));
                                                break;
                                            } else if (multimediaJson.getInt("width") == 190) {
                                                article.setMultimedia_url(multimediaJson.getString("url"));
                                            } else {
                                                article.setMultimedia_url(multimediaJson.getString("url"));
                                            }
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
                             }
                        else {
                            Toast.makeText(SearchActivity.this, "No data found, please change your filters or search for a different article", Toast.LENGTH_LONG).show();
                        }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }



                }

                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }
            });
        }
        else{
            Toast.makeText(SearchActivity.this, "Can you please check your internet connection and re-try", Toast.LENGTH_LONG).show();
        }

    }

    public void filterArticles(View view) {

        Intent intent = new Intent(this, FilterActivity.class);
        ArticleFilter articleFilter = new ArticleFilter(date,order,arts,fashion,sports);
        intent.putExtra("articleFilter", Parcels.wrap(articleFilter));
        startActivityForResult(intent, REQUEST_CODE_DISPLAY);

    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DISPLAY) {
            date = data.getExtras().getString("date");
            order = data.getExtras().getString("order");
            arts = data.getExtras().getBoolean("arts");
            fashion = data.getExtras().getBoolean("fashion");
            sports = data.getExtras().getBoolean("sports");
            if(allArticles != null)
                allArticles.clear();
            if(rcAdapter!= null)
                rcAdapter.notifyDataSetChanged();
            noOfPages = 1;
            noOfElements = 0;
            getArticleData();

        }
    }

}
