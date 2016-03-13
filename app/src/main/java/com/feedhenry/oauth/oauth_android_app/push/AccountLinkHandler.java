package com.feedhenry.oauth.oauth_android_app.push;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.feedhenry.oauth.oauth_android_app.content.contract.FehAccountContract;
import com.feedhenry.oauth.oauth_android_app.content.vo.FehAccount;

public final class AccountLinkHandler {

    public static void SaveAccountAsLinked(Context context, Bundle message) {
        Cursor cursor = context.getContentResolver().query(FehAccountContract.URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            FehAccount account = new FehAccount(cursor.getString(FehAccountContract.SUB_IDX),
                    cursor.getString(FehAccountContract.PICTURE_IDX),
                    cursor.getString(FehAccountContract.NAME_IDX),
                    cursor.getString(FehAccountContract.EMAIL_IDX));
            account.setVerified(true);
            account.save(context);
        }
        cursor.close();
    }

}
