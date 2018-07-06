package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetails extends AppCompatActivity {
    private TwitterClient client;
    Tweet tweet;
    public ImageView tvProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvDate;
    public TextView tvScreenName;
    public TextView tvFavoriteClick;
    public TextView tvNumFavorite;
    public TextView tvRetweetClick;
    public TextView tvNumRetweet;
    boolean favorited;
    boolean retweeted;
    boolean isClickedF;
    boolean isClickedR;
    int numFavorite;
    int numRetweet;
    long uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        client = TwitterApp.getRestClient(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        tvProfileImage = findViewById(R.id.tvProfileImage);
        tvUsername = findViewById(R.id.tvUserName);
        tvBody = findViewById(R.id.tvBody);
        tvDate = findViewById(R.id.tvDate);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvFavoriteClick = findViewById(R.id.tvFavoriteClick);
        tvNumFavorite = findViewById(R.id.tvNumFavorite);
        tvRetweetClick = findViewById(R.id.tvRetweetClick);
        tvNumRetweet = findViewById(R.id.tvNumRetweet);
        uid = tweet.uid;
        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvDate.setText(tweet.createdAt);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvNumRetweet.setText(String.valueOf(tweet.retweet_count));
        tvNumFavorite.setText(String.valueOf(tweet.user.favourites_count));
        retweeted = tweet.retweeted;
        favorited = tweet.favorited;
        numFavorite = Integer.parseInt(tvNumFavorite.getText().toString());
        numRetweet = Integer.parseInt(tvNumRetweet.getText().toString());
        if(favorited) {
            tvFavoriteClick.setTypeface(null, Typeface.BOLD);
            isClickedF = true;
        }
        if(retweeted) {
            tvRetweetClick.setTypeface(null, Typeface.BOLD);
            isClickedR = true;
        }

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(75, 0)))
                .into(tvProfileImage);

        tvFavoriteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView v = (TextView)view;
                if (isClickedF) {
                    v.setTypeface(null, Typeface.NORMAL);
                    isClickedF = false;
                    tvNumFavorite.setText(Integer.toString(--numFavorite));
                    client.sendUnfavorite(uid, new JsonHttpResponseHandler() {});
                }
                else {
                    v.setTypeface(null, Typeface.BOLD);
                    isClickedF = true;
                    tvNumFavorite.setText(Integer.toString(++numFavorite));
                    client.sendFavorite(uid, new JsonHttpResponseHandler() {});
                }


            }
        });
        tvRetweetClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView v = (TextView)view;
                if (isClickedR) {
                    v.setTypeface(null, Typeface.NORMAL);
                    isClickedR = false;
                    tvNumRetweet.setText(Integer.toString(--numRetweet));
                    client.sendUnretweet(uid, new JsonHttpResponseHandler() {});
                }
                else {
                    v.setTypeface(null, Typeface.BOLD);
                    isClickedR = true;
                    tvNumRetweet.setText(Integer.toString(++numRetweet));
                    client.sendRetweet(uid, new JsonHttpResponseHandler() {});
                }

            }
        });
    }



}