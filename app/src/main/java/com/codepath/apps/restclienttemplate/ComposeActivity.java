package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    EditText tvTweet;
    TextView tvCharCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tvTweet = findViewById(R.id.tvTweet);
        tvCharCount = findViewById(R.id.tvCharCount);

        String userToReply = getIntent().getStringExtra(TimelineActivity.REPLY_TEXT);
        if (userToReply!=null) {
            String formattedReply = "@" + userToReply + " ";
            tvTweet.setText(formattedReply);
            tvTweet.setSelection(formattedReply.length()); // +2 account for @ and " "
            tvCharCount.setText(Integer.toString(140 - formattedReply.length()));
        }


        // character counter
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvCharCount.setText(Integer.toString(140 - s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        tvTweet.addTextChangedListener(mTextEditorWatcher);
    }

    public void onSendTweet(View v) {
        Intent data = new Intent(); // prepare intent to pass back to MainActivity
        data.putExtra(TimelineActivity.TWEET_TEXT, tvTweet.getText().toString()); // sends back tweet
        setResult(RESULT_OK, data); // set result code + bundle response
        finish(); // closes edit activity + passes intent data back to main

    }


}
