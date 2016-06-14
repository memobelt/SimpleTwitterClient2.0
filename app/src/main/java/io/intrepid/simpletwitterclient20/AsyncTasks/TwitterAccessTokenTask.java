package io.intrepid.simpletwitterclient20.AsyncTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.net.HttpURLConnection;

import io.intrepid.simpletwitterclient20.Constants;

public class TwitterAccessTokenTask extends AsyncTask<String, Void, Boolean> {
    private OAuth1RequestToken requestToken;
    private OAuth10aService service;
    SharedPreferences tokens;

    public TwitterAccessTokenTask(OAuth1RequestToken requestToken, OAuth10aService service, SharedPreferences tokens) {
        this.requestToken = requestToken;
        this.service = service;
        this.tokens = tokens;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            OAuth1AccessToken auth1AccessToken = service.getAccessToken(requestToken, params[0]);
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json", service);
            service.signRequest(auth1AccessToken, request);
            SharedPreferences.Editor editor = tokens.edit();
            editor.putString(Constants.TOKEN, auth1AccessToken.getToken());
            editor.putString(Constants.TOKEN_SECRET, auth1AccessToken.getTokenSecret());
            editor.apply();
            final Response response = request.send();
            return response.getCode() == HttpURLConnection.HTTP_OK;
        }
        //in case the pin code is wrong
        catch (Exception e) {
            return false;
        }
    }
}
