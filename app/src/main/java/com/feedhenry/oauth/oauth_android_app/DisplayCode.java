package com.feedhenry.oauth.oauth_android_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.feedhenry.oauth.oauth_android_app.push.AccountLinkHandler;

import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DisplayCode extends AppCompatActivity implements MessageHandler {

    @Bind(R.id.code) TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_code);
        ButterKnife.bind(this);
        code.setText(getIntent().getStringExtra("code"));

    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        RegistrarManager.registerMainThreadHandler(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        RegistrarManager.unregisterMainThreadHandler(this);

    }


    @Override
    public void onMessage(Context context, Bundle message) {
        if (message.getString("alert").equals("{\"action\":\"link\"}")) {
            AccountLinkHandler.SaveAccountAsLinked(context, message);
            Intent displayCodeIntent = new Intent(this, ShowMessages.class);
            startActivity(displayCodeIntent);
            finish();
        }
    }

    @Override
    public void onError() {

    }
}
