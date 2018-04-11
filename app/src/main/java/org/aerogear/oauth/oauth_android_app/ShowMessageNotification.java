package org.aerogear.oauth.oauth_android_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.aerogear.oauth.oauth_android_app.content.contract.FehAccountContract;
import org.aerogear.oauth.oauth_android_app.content.vo.FehAccount;
import org.aerogear.oauth.oauth_android_app.content.vo.FehMessage;
import org.aerogear.oauth.oauth_android_app.push.AccountLinkHandler;
import org.aerogear.oauth.oauth_android_app.push.FehMessageHandler;
import org.aerogear.oauth.oauth_android_app.util.ImageFromNameMaker;

import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.gcm.UnifiedPushMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by summers on 3/14/16.
 */
public class ShowMessageNotification implements MessageHandler {

    private static final int NOTIFICATION_ID = 0x100;

    @Override
    public void onDeleteMessage(Context context, Bundle message) {

    }

    @Override
    public void onMessage(Context context, Bundle message) {
        String messageString = message.getString("alert");
        if (messageString.equals("{\"action\":\"link\"}")) {
            AccountLinkHandler.SaveAccountAsLinked(context, message);
        } else {
            JSONObject object = null;
            try {
                object = new JSONObject(messageString);

                if (object.optString("action") != null) {
                    FehMessage fehMessage = FehMessageHandler.saveMessage(context, message);
                    showNotification(context, fehMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showNotification(Context context, FehMessage fehMessage ) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, ShowMessages.class)
                .addFlags(PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ag)
                .setLargeIcon(ImageFromNameMaker.makeImage(context, fehMessage.getFromName()).getBitmap())
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(fehMessage.getMessage()))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(fehMessage.getMessage());

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onError() {

    }
}
