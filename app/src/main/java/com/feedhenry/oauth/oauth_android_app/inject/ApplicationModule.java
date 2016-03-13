package com.feedhenry.oauth.oauth_android_app.inject;

import android.content.Context;

import com.feedhenry.oauth.oauth_android_app.FehBotApplication;
import com.feedhenry.oauth.oauth_android_app.OAuthWelcome;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                FehBotApplication.class, OAuthWelcome.class
        }
)
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    public GoogleApiClient provideGoogleAPIClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestServerAuthCode("272275396485-ke3ehqieoois0g69r66dh3ap7uc3d56h.apps.googleusercontent.com")
                    .requestIdToken("272275396485-ke3ehqieoois0g69r66dh3ap7uc3d56h.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        return apiClient;
    }


}
