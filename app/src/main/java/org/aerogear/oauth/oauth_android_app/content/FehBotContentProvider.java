package org.aerogear.oauth.oauth_android_app.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.aerogear.oauth.oauth_android_app.content.contract.FehAccountContract;
import org.aerogear.oauth.oauth_android_app.content.contract.FehMessageContract;
import org.aerogear.oauth.oauth_android_app.content.vo.FehAccount;
import org.aerogear.oauth.oauth_android_app.content.vo.FehMessage;

public class FehBotContentProvider extends ContentProvider {
    private SharedPreferences store;
    private SQLiteDatabase messageDb;

    public FehBotContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri.equals(FehAccountContract.URI)) {
            if (store.getString(FehAccountContract.SUB, "").isEmpty()) {
                return 0;
            } else {
                store.edit().clear().apply();
                this.getContext().getContentResolver().notifyChange(uri, null);
                return 1;
            }
        } else {
            throw new IllegalArgumentException(String.format("%s not supported", uri.toString()));
        }
    }

    @Override
    public String getType(Uri uri) {
        if (uri.equals(FehAccountContract.URI)) {
            return uri.toString();
        } else if (uri.equals(FehMessageContract.URI)) {
            return uri.toString();
        } else {
            throw new IllegalArgumentException(String.format("%s not supported", uri.toString()));
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uri.equals(FehAccountContract.URI)) {
            store.edit().clear()
                    .putString(FehAccountContract.SUB, values.getAsString(FehAccountContract.SUB))
                    .putString(FehAccountContract.PICTURE, values.getAsString(FehAccountContract.PICTURE))
                    .putString(FehAccountContract.EMAIL, values.getAsString(FehAccountContract.EMAIL))
                    .putString(FehAccountContract.NAME, values.getAsString(FehAccountContract.NAME))
                    .putBoolean(FehAccountContract.VERIFIED, values.getAsBoolean(FehAccountContract.VERIFIED))
                    .apply();
            if (values.getAsBoolean(FehAccountContract.NOTIFY)) {
                this.getContext().getContentResolver().notifyChange(uri, null);
            }
            return uri;
        } else if (uri.equals(FehMessageContract.URI)) {
            ContentValues newValues = new ContentValues(values);
            newValues.remove(FehMessageContract.NOTIFY);
            messageDb.beginTransaction();
            messageDb.insertOrThrow(FehMessageContract.FEH_MESSAGE_TABLE, null, newValues);
            messageDb.setTransactionSuccessful();
            messageDb.endTransaction();
            if (values.getAsBoolean(FehMessageContract.NOTIFY)) {
                this.getContext().getContentResolver().notifyChange(uri, null);
            }
            return uri;

        } else {
            throw new IllegalArgumentException(String.format("%s not supported", uri.toString()));
        }
    }

    @Override
    public boolean onCreate() {
        this.store = super.getContext().getSharedPreferences("FehAccount", 0);
        this.messageDb = new FehSQLiteOpenHelper(this.getContext()).getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (uri.equals(FehAccountContract.URI)) {

            if (store.getString(FehAccountContract.SUB, "").isEmpty()) {
                return new FehAccountContract.FehAccountCursor(null);
            } else {
                FehAccount account = new FehAccount(store.getString(FehAccountContract.SUB, ""),
                        store.getString(FehAccountContract.PICTURE, ""),
                        store.getString(FehAccountContract.NAME, ""),
                        store.getString(FehAccountContract.EMAIL, "")
                );

                account.setVerified(store.getBoolean(FehAccountContract.VERIFIED, false));

                return new FehAccountContract.FehAccountCursor(account);
            }

        } else if (uri.equals(FehMessageContract.URI)) {
            return messageDb.query(false, FehMessageContract.FEH_MESSAGE_TABLE, FehMessageContract.COLUMNS.toArray(new String[5]), null, null, null, null, "date desc", null);

        } else {
            throw new IllegalArgumentException(String.format("%s not supported", uri.toString()));
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uri.equals(FehAccountContract.URI)) {
            store.edit().clear()
                    .putString(FehAccountContract.SUB, values.getAsString(FehAccountContract.SUB))
                    .putString(FehAccountContract.PICTURE, values.getAsString(FehAccountContract.PICTURE))
                    .putString(FehAccountContract.EMAIL, values.getAsString(FehAccountContract.EMAIL))
                    .putString(FehAccountContract.NAME, values.getAsString(FehAccountContract.NAME))
                    .putBoolean(FehAccountContract.VERIFIED, values.getAsBoolean(FehAccountContract.VERIFIED))
                    .apply();
            if (values.getAsBoolean(FehAccountContract.NOTIFY)) {
                this.getContext().getContentResolver().notifyChange(uri, null);
            }
            return 1;
        } else {
            throw new IllegalArgumentException(String.format("%s not supported", uri.toString()));
        }
    }
}
