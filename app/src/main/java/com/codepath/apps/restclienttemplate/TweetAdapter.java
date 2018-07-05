package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    public static List<Tweet> mTweets;
    public static Context context;
    // pass in tweets array in constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;

    }
    // for each row, inflate layout and cache references in viewholder
    // invoked when i need to make a new row
    // dont have to keep making rows
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind values based on position of element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data according to position
        Tweet tweet = mTweets.get(position);

        // populate views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvDate.setText(tweet.createdAt);
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(75, 0)))
                .into(holder.ivProfileImage);
    }

    // Clean all elements of the recycler || For refreshing
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used || For refreshing
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    // crreate viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvDate;
        public TextView tvScreenName;
        public TextView tvReply;
        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvReply = itemView.findViewById(R.id.tvReply);

            tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition(); // item pos
                    if (position != RecyclerView.NO_POSITION) { // if position is valid
                        Tweet tweet = mTweets.get(position); // get movie at current position
                        Intent intent = new Intent(context, ComposeActivity.class); // makes intent
                        intent.putExtra(TimelineActivity.REPLY_TEXT, tweet.user.screenName);
                        // ^^ serializes movie using parceler. it uses short name as key, movie as value
                        ((Activity)context).startActivityForResult(intent, TimelineActivity.EDIT_REQUEST_CODE);
                    }
                }
            });

    }
    }
//
//    public void onReply(MenuItem mi) {
//        // handle click here
//        int position = getAdapterPosition();
//        Intent intent = new Intent(this, ComposeActivity.class);
//        intent.putExtra(ComposeActivity.REPLY_TEXT, mi.getText().toString());
//        this.startActivityForResult(intent, EDIT_REQUEST_CODE);
//    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
