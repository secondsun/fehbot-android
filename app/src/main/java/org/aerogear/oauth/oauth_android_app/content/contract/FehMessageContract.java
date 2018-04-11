package org.aerogear.oauth.oauth_android_app.content.contract;

import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;


import org.aerogear.oauth.oauth_android_app.content.vo.FehMessage;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by summers on 3/16/16.
 */
public class FehMessageContract {
    public static final Uri URI = Uri.parse("content://org.feedhenry.fehbot/FehMessage");


    public static final String FROM_PICTURE = "picture";
    public static final String FROM_NAME = "name";
    public static final String SENT_DATE = "date";
    public static final String MESSAGE = "message";
    public static final String _ID = "id";
    public static final String FEH_MESSAGE_TABLE = "message";
    public static String NOTIFY = "NOTIFY";

    public static final List<String> COLUMNS = Arrays.asList(new String[]{_ID, FROM_PICTURE, FROM_NAME, MESSAGE, SENT_DATE});

    public static final int MESSAGE_IDX = COLUMNS.indexOf(MESSAGE);
    public static final int FROM_PICTURE_IDX = COLUMNS.indexOf(FROM_PICTURE);
    public static final int FROM_NAME_IDX = COLUMNS.indexOf(FROM_NAME);
    public static final int SENT_DATE_IDX = COLUMNS.indexOf(SENT_DATE);


    public static ContentValues valueize(FehMessage message, boolean notify) {
        ContentValues values = new ContentValues();

        values.put(FROM_PICTURE, message.getFromPicture());
        values.put(FROM_NAME, message.getFromName());
        values.put(MESSAGE, message.getMessage());
        values.put(SENT_DATE, message.getSentDate().getTime());
        values.put(NOTIFY, notify);

        return values;
    }

    public static FehMessage convertRowToMessage(Cursor cursor) {

        String from = cursor.getString(FROM_NAME_IDX);
        String message = cursor.getString(MESSAGE_IDX);
        String picture = cursor.getString(FROM_PICTURE_IDX);
        long date = cursor.getLong(SENT_DATE_IDX);

        return new FehMessage(picture, from, message, new Date(date));

    }

}
