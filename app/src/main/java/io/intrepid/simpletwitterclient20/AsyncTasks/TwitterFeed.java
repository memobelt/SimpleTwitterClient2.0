package io.intrepid.simpletwitterclient20.AsyncTasks;

import android.os.AsyncTask;

import io.intrepid.simpletwitterclient20.Constants;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFeed extends AsyncTask<Void, Void, ResponseList<Status>> {
    private static final String TAG = TwitterFeed.class.getSimpleName();
    String tokenSecret;
    String token;

    public TwitterFeed(String tokenSecret, String token) {
        this.tokenSecret = tokenSecret;
        this.token = token;
    }

    @Override
    protected ResponseList<twitter4j.Status> doInBackground(Void... params) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.API_KEY)
                .setOAuthConsumerSecret(Constants.SECRET_KEY)
                .setOAuthAccessToken(token)
                .setOAuthAccessTokenSecret(tokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        final Twitter twitter = tf.getInstance();
        try {
            return twitter.getHomeTimeline();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
