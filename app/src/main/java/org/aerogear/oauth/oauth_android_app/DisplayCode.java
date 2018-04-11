package org.aerogear.oauth.oauth_android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.aerogear.mobile.push.MessageHandler;
import org.aerogear.mobile.push.PushService;
import org.aerogear.oauth.oauth_android_app.push.AccountLinkHandler;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayCode extends AppCompatActivity implements MessageHandler {

    @BindView(R2.id.code) TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_code);
        ButterKnife.bind(this);
        code.setText(getIntent().getStringExtra("code"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        PushService.registerMainThreadHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PushService.unregisterMainThreadHandler(this);
    }


    @Override
    public void onMessage(Context context, Map<String, String> message) {
        if (message.get("alert").equals("{\"action\":\"link\"}")) {
            AccountLinkHandler.SaveAccountAsLinked(context, message);
            Intent displayCodeIntent = new Intent(this, ShowMessages.class);
            startActivity(displayCodeIntent);
        }
    }
}
