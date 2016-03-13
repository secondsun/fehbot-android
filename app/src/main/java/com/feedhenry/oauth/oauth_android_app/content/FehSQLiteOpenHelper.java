package com.feedhenry.oauth.oauth_android_app.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.feedhenry.oauth.oauth_android_app.content.contract.FehAccountContract;
import com.feedhenry.oauth.oauth_android_app.content.contract.FehMessageContract;
import com.feedhenry.oauth.oauth_android_app.content.vo.FehMessage;

import java.util.List;

/**
 * Created by summers on 3/16/16.
 */
public class FehSQLiteOpenHelper extends SQLiteOpenHelper {

    public FehSQLiteOpenHelper(Context context) {
        super(context, "Feh.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FehMessageContract.FEH_MESSAGE_TABLE + "("
                + FehMessageContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FehMessageContract.FROM_PICTURE + " TEXT, "
                + FehMessageContract.FROM_NAME+ " TEXT NOT NULL, "
                + FehMessageContract.MESSAGE+ " TEXT NOT NULL, "
                + FehMessageContract.SENT_DATE+ " LONG NOT NULL"

                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }


}
