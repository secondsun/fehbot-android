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
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.feedhenry.oauth.oauth_android_app.content.contract.FehAccountContract;
import com.feedhenry.oauth.oauth_android_app.content.contract.FehMessageContract;
import com.feedhenry.oauth.oauth_android_app.content.vo.FehAccount;
import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;
import com.feedhenry.sdk.PushConfig;
import com.feedhenry.sdk.api.FHAuthRequest;
import com.feedhenry.sdk.api.FHAuthSession;
import com.feedhenry.sdk.api.FHCloudRequest;
import com.feedhenry.sdk.utils.DataManager;
import com.feedhenry.sdk2.FHHttpClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.fh.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This abstract class contains all of the important FHOAuth code.
 */
public abstract class FHOAuth extends AppCompatActivity {

    private static final String TAG = "FHAuthActivity";
    private static final String FH_AUTH_POLICY = "Android";

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FH.stop();
    }

    private void init() {
        FH.init(this, new FHActCallback() {
            @Override
            public void success(FHResponse pResponse) {
                Log.d(TAG, "FH.init - success");
                try {
                    onFHReady();
                } catch (Exception e) {
                    Toast.makeText(FHOAuth.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void fail(FHResponse pResponse) {
                Toast.makeText(FHOAuth.this, "Init failed", Toast.LENGTH_LONG).show();
                Log.d(TAG, "FH.init - fail");
                Log.e(TAG, pResponse.getErrorMessage(), pResponse.getError());
            }
        });

    }

    protected void checkSession() throws Exception {

        //To check if user is already authe
        // nticated
        boolean exists = FHAuthSession.exists();
        if (exists) {
            Cursor cursor = getContentResolver().query(FehAccountContract.URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                if (cursor.getShort(FehAccountContract.VERIFIED_IDX) > 0) {

                    PushConfig config = new PushConfig();
                    config.setAlias(cursor.getString(FehAccountContract.SUB_IDX));
                    FH.pushRegister(config, new FHActCallback() {
                        @Override
                        public void success(FHResponse fhResponse) {
                            Intent displayCodeIntent = new Intent(FHOAuth.this, ShowMessages.class);
                            startActivity(displayCodeIntent);
                            finish();
                        }

                        @Override
                        public void fail(FHResponse fhResponse) {
                            Toast.makeText(FHOAuth.this, fhResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    onNotLoggedIn();
                }
            } else {
                onNotLoggedIn();
            }
            cursor.close();

        } else {
            onNotLoggedIn();
        }

    }

    protected void doAuth(GoogleSignInResult result, final String ircNick) {
        FHAuthRequest authRequest = new FHAuthRequest(getApplicationContext(), new com.feedhenry.sdk.api2.FHAuthSession(DataManager.getInstance(), new FHHttpClient()));
        authRequest.setAuthUser("Android", result.getSignInAccount().getIdToken(), "!");
        try {
            authRequest.executeAsync(new FHActCallback() {
                @Override
                public void success(FHResponse fhResponse) {
                    //Get Account
                    FHCloudRequest req = new FHCloudRequest(getApplicationContext());
                    req.setPath("/account/me");
                    req.setMethod(FHCloudRequest.Methods.GET);
                    try {
                        req.executeAsync(new FHActCallback() {
                            @Override
                            public void success(FHResponse fhResponse) {
                                //sub,picture, email,name
                                JSONObject response = fhResponse.getJson();
                                String sub = response.getString("sub");
                                String picture = response.getString("picture");
                                String email = response.getString("email");
                                String name = response.getString("name");

                                final FehAccount account = new FehAccount(sub, picture, name, email);

                                PushConfig config = new PushConfig();
                                config.setAlias(sub);
                                FH.pushRegister(config, new FHActCallback() {
                                    @Override
                                    public void success(FHResponse fhResponse) {
                                        saveAndLinkAccount(account, ircNick);
                                    }

                                    @Override
                                    public void fail(FHResponse fhResponse) {
                                        Toast.makeText(FHOAuth.this, fhResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                            @Override
                            public void fail(FHResponse fhResponse) {
                                Toast.makeText(FHOAuth.this, fhResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(FHResponse fhResponse) {
                    onNotLoggedIn();
                }
            });
        } catch (Exception ignore) {

        }
    }

    protected void saveAndLinkAccount(FehAccount account, String ircNick){
        account.save(this);
        FHCloudRequest req = new FHCloudRequest(this);
        req.setMethod(FHCloudRequest.Methods.POST);
        req.setPath("/account/link");
        req.setRequestArgs(new JSONObject().append("ircNick",ircNick).  append("sub", account.getSub()).append( "name", account.getName()).append("email", account.getName()).append("picture", account.getPicture()));
        try {
            req.executeAsync(new FHActCallback() {
                @Override
                public void success(FHResponse fhResponse) {
                    String code = fhResponse.getJson().optString("code");
                    onInitComplete(code);
                }

                @Override
                public void fail(FHResponse fhResponse) {
                    Toast.makeText(FHOAuth.this, fhResponse.getErrorMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(FHOAuth.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Called when Feed Henry is ready to be used.
     */
    public abstract void onFHReady();

    /**
     * This is called after FH has determined the user is not logged in.
     */
    public abstract void onNotLoggedIn();

    /**
     * Called when the application is fully set up
     */
    public abstract void onInitComplete(String code);


}
