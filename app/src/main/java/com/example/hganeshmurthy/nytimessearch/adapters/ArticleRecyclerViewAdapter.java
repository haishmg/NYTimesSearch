package com.example.hganeshmurthy.nytimessearch.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hganeshmurthy.nytimessearch.NYTArticle;
import com.example.hganeshmurthy.nytimessearch.R;
import com.example.hganeshmurthy.nytimessearch.activities.DisplayArticle;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by hganeshmurthy on 2/12/16.
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ArticleViewHolders> {

    private ArrayList<NYTArticle> articleList;
    private Context context;
    int pos;

    public  ArticleRecyclerViewAdapter(Context context, ArrayList<NYTArticle> articleList) {
        this.articleList = articleList;
        this.context = context;
    }

    @Override
    public ArticleViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list, null);
        ArticleViewHolders rcv = new ArticleViewHolders(layoutView);
        return rcv;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolders holder, int position) {
        holder.tvTitle.setText(articleList.get(position).getPrint_headline().toString());
        holder.ivPhoto.setImageResource(0);
        holder.articleUrl = articleList.get(position).web_url.toString();
        String url ="http://www.nytimes.com/" + articleList.get(position).getMultimedia_url();
        Glide.with(context).load(url).placeholder(R.drawable.progress_animation).fitCenter().into(holder.ivPhoto);


    }

    private int[] getViewLocations(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        return locations;
    }

    @Override
    public int getItemCount() {
        return this.articleList.size();
    }

    public class ArticleViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener  {

        ImageView ivPhoto;
        TextView tvTitle;
        String articleUrl;


        public ArticleViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }

        public void onClick(View v) {
            //Toast.makeText(v.getContext(), "Clicked Position view holder = " +getPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), DisplayArticle.class);
            NYTArticle article =  articleList.get(getPosition());
            intent.putExtra("article", Parcels.wrap(article));
            v.getContext().startActivity(intent);

        }

    }
}
