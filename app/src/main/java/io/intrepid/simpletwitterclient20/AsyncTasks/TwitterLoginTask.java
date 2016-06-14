package io.intrepid.simpletwitterclient20.AsyncTasks;

import android.os.AsyncTask;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import io.intrepid.simpletwitterclient20.Constants;

public class TwitterLoginTask extends AsyncTask<String, Void, String> {

    private OAuth1RequestToken requestToken;
    private OAuth10aService service;

    public OAuth1RequestToken getRequestToken() {
        return requestToken;
    }

    public OAuth10aService getService() {
        return service;
    }

    @Override
    protected String doInBackground(String... params) {
        service = new ServiceBuilder()
                .apiKey(Constants.API_KEY)
                .apiSecret(Constants.SECRET_KEY)
                .build(TwitterApi.instance());
        requestToken = service.getRequestToken();
        return service.getAuthorizationUrl(requestToken);
    }
}