/**
 * Copyright 2015 Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feedhenry.oauth.oauth_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;
import com.feedhenry.sdk.api.FHAuthRequest;
import com.feedhenry.sdk.api.FHAuthSession;
import com.feedhenry.sdk.utils.DataManager;
import com.feedhenry.sdk2.FHHttpClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class setups up the UI of the application
 */
public class OAuthWelcome extends FHOAuth {

    private static final int RC_SIGN_IN = 0x01100;
    private TextView response;
    private View progressBar;
    private View logInButton;
    private EditText ircNick;
    @Inject GoogleApiClient googleApiClient;

    private static final String TAG = "FHAuthActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_welcome);
        response = (TextView) findViewById(R.id.response);
        progressBar = findViewById(R.id.progress_bar);
        logInButton = findViewById(R.id.log_in);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        ircNick = (EditText) findViewById(R.id.irc_nick);
        ((FehBotApplication) getApplicationContext()).getObjectGraph().inject(this);

    }


    @OnClick(R.id.log_in)
    public void login() {
        if (ircNick.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must enter an IRC nick.", Toast.LENGTH_LONG).show();
            return;
        }
        GoogleApiClient apiClient = googleApiClient;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, OAuthWelcome.RC_SIGN_IN);
    }

    @Override
    public void onFHReady() {
        try {
            checkSession();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onNotLoggedIn() {
        response.setText(getString(R.string.not_logged_in_message));
        progressBar.setVisibility(View.GONE);
        logInButton.setVisibility(View.VISIBLE);
        ircNick.setVisibility(View.VISIBLE);
    }


    @Override
    public void onInitComplete(String code) {
        if (code != null) {
            Intent displayCodeIntent = new Intent(this, DisplayCode.class);
            displayCodeIntent.putExtra("code", code);
            startActivity(displayCodeIntent);
            finish();
        } else {
            Intent displayCodeIntent = new Intent(this, ShowMessages.class);
            startActivity(displayCodeIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RC_SIGN_IN == requestCode) {
            if (resultCode != RESULT_OK) {
                return;
            }
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            doAuth(result, ircNick.getText().toString());


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
