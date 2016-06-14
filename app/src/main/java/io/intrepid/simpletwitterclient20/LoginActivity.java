package io.intrepid.simpletwitterclient20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.simpletwitterclient20.AsyncTasks.TwitterAccessTokenTask;
import io.intrepid.simpletwitterclient20.AsyncTasks.TwitterLoginTask;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences tokens;
    private OAuth10aService service;
    private OAuth1RequestToken requestToken;
    String authUrl;

    @BindView(R.id.et_pin)
    EditText etPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokens = getSharedPreferences(Constants.AUTH, MODE_PRIVATE);
        if (!tokens.getString(Constants.TOKEN, "").isEmpty()) {
            launchFeed();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        twitterSetUp();
    }

    public void twitterSetUp() {
        TwitterLoginTask twitterLoginTask = new TwitterLoginTask();
        try {
            authUrl = twitterLoginTask.execute().get();
            service = twitterLoginTask.getService();
            requestToken = twitterLoginTask.getRequestToken();
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(this, R.string.error_connecting, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_ok)
    public void launchTwitterLogin() {
        if (authUrl != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
        } else {
            Toast.makeText(LoginActivity.this, R.string.error_connecting, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_enter)
    public void checkPin() {
        String pin = etPin.getText().toString();
        if (pin.isEmpty()) {
            etPin.setError(getString(R.string.missing_pin));
        } else {
            try {
                Boolean hasAccessToken =
                        new TwitterAccessTokenTask(requestToken, service, tokens).execute(pin).get();
                if (!hasAccessToken) {
                    etPin.setError(getString(R.string.wrong_pin));
                } else {
                    launchFeed();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void launchFeed() {
        startActivity(new Intent(this, FeedActivity.class));
        finish();
    }
}
