package com.feedhenry.oauth.oauth_android_app.content.vo;

import android.content.Context;

import com.feedhenry.oauth.oauth_android_app.content.contract.FehAccountContract;

/**
 * Created by summers on 3/13/16.
 */
public class FehAccount {
    private final String sub;
    private final String picture;
    private final String name;
    private final String email;
    private boolean verified = false;


    public FehAccount(String sub, String picture, String name, String email) {
        this.sub = sub;
        this.picture = picture;
        this.name = name;
        this.email = email;
    }

    public String getSub() {
        return sub;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void save(Context context) {
        context.getContentResolver().update(FehAccountContract.URI,FehAccountContract.valueize(this,true), "", null);
    }

    public void delete(Context context) {
        context.getContentResolver().delete(FehAccountContract.URI,"",null);
    }

}
