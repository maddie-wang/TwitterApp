package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
    public TextView tvNumFavorite;
    public ImageButton tvHeart;
    public ImageButton tvRetweet;
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
        tvNumFavorite = findViewById(R.id.tvNumFavorite);
        tvNumRetweet = findViewById(R.id.tvNumRetweet);
        tvHeart = findViewById(R.id.tvHeart);
        tvRetweet = findViewById(R.id.tvRetweet);
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
            tvHeart.setBackgroundResource(R.drawable.ic_vector_heart);
            tvHeart.getDrawable().setColorFilter(getResources().getColor(R.color.medium_red), PorterDuff.Mode.SRC_ATOP);
            tvHeart.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00e0245e));
            isClickedF = true;
        }
        if(retweeted) {
            tvRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
            tvRetweet.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x0017bf63));
            isClickedR = true;
        }

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(75, 0)))
                .into(tvProfileImage);

        tvHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton v = (ImageButton)view;
                if (isClickedF) {
                    isClickedF = false;
                    v.setBackgroundResource(android.R.color.transparent);
                    v.getDrawable().setColorFilter(getResources().getColor(R.color.medium_gray), PorterDuff.Mode.SRC_ATOP);
                    tvNumFavorite.setText(Integer.toString(--numFavorite));
                    client.sendUnfavorite(uid, new JsonHttpResponseHandler() {});
                }
                else {
                    isClickedF = true;
                    v.setBackgroundResource(R.drawable.ic_vector_heart);
                    v.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00e0245e));
                    v.getDrawable().setColorFilter(getResources().getColor(R.color.medium_red), PorterDuff.Mode.SRC_ATOP);
                    tvNumFavorite.setText(Integer.toString(++numFavorite));
                    client.sendFavorite(uid, new JsonHttpResponseHandler() {});
                }


            }
        });
        tvRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton v = (ImageButton)view;
                if (isClickedR) {
                    v.setBackgroundResource(android.R.color.transparent);
                    isClickedR = false;
                    tvNumRetweet.setText(Integer.toString(--numRetweet));
                    client.sendUnretweet(uid, new JsonHttpResponseHandler() {});
                }
                else {
                    v.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                    v.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x0017bf63));
                    isClickedR = true;
                    tvNumRetweet.setText(Integer.toString(++numRetweet));
                    client.sendRetweet(uid, new JsonHttpResponseHandler() {});
                }

            }
        });
    }



}