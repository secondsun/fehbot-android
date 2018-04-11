package org.aerogear.oauth.oauth_android_app.push;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.aerogear.oauth.oauth_android_app.content.vo.FehMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public final class FehMessageHandler {

    public static FehMessage saveMessage(Context context, Bundle message) {
        try {
            String messageString = message.getString("alert");
            JSONObject object = new JSONObject(messageString);
            String picture = object.optString("picture");
            Long date = object.getLong("date");
            String from = object.getString("from");
            String messageText = object.getString("message");
            FehMessage fehMessage = new FehMessage(picture, from, messageText, new Date(date));
            fehMessage.save(context);
            return fehMessage;
        } catch (JSONException e) {
            Log.e("PUSH_HANDLER", e.getMessage(), e);
        }
        return new FehMessage("","","", new Date());
    }

}
