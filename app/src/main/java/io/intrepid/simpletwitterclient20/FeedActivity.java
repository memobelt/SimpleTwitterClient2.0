package io.intrepid.simpletwitterclient20;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.intrepid.simpletwitterclient20.AsyncTasks.TwitterFeedTask;
import io.intrepid.simpletwitterclient20.adapter.TweetAdapter;
import twitter4j.Status;

public class FeedActivity extends AppCompatActivity {
    List<Status> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        SharedPreferences preferences = getSharedPreferences(Constants.AUTH, MODE_PRIVATE);
        String token = preferences.getString(Constants.TOKEN, "");
        String tokenSecret = preferences.getString(Constants.TOKEN_SECRET, "");
        loadFeed(tokenSecret, token);
    }

    private void loadFeed(String tokenSecret, String token) {
        try {
            tweets = new TwitterFeedTask(tokenSecret, token).execute().get();
            if(tweets == null){
                Toast.makeText(FeedActivity.this, R.string.problem_loading, Toast.LENGTH_SHORT).show();
            }
            TweetAdapter tweetAdapter = new TweetAdapter(tweets, this);
            setUpRecyclerView(tweetAdapter);
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(FeedActivity.this, R.string.problem_loading, Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpRecyclerView(TweetAdapter tweetAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView rvTweets = (RecyclerView) findViewById(R.id.rv_tweets);
        assert rvTweets != null;
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);
        rvTweets.setVisibility(View.VISIBLE);
    }
}
