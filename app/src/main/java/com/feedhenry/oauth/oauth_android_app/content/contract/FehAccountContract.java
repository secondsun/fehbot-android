package com.feedhenry.oauth.oauth_android_app.content.contract;

import android.content.ContentValues;
import android.database.AbstractCursor;
import android.net.Uri;

import com.feedhenry.oauth.oauth_android_app.content.vo.FehAccount;

import java.util.Arrays;
import java.util.List;

/**
 * Created by summers on 3/13/16.
 */
public class FehAccountContract {
    public static final Uri URI = Uri.parse("content://org.feedhenry.fehbot/FehAccount");
    public static final String SUB = "SUB";
    public static final String PICTURE = "PICTURE";
    public static final String EMAIL = "EMAIL";
    public static final String NAME = "NAME";
    public static final String VERIFIED = "VERIFIED";


    public static String NOTIFY = "NOTIFY";

    public static final List<String> COLUMNS = Arrays.asList(new String[]{SUB, PICTURE, NAME, EMAIL, VERIFIED, NOTIFY});

    public static final int SUB_IDX = COLUMNS.indexOf(SUB);
    public static final int PICTURE_IDX = COLUMNS.indexOf(PICTURE);
    public static final int EMAIL_IDX = COLUMNS.indexOf(EMAIL);
    public static final int NAME_IDX = COLUMNS.indexOf(NAME);
    public static final int VERIFIED_IDX = COLUMNS.indexOf(VERIFIED);


    public static ContentValues valueize(FehAccount account, boolean notify) {
        ContentValues values = new ContentValues();
        values.put(SUB, account.getSub());
        values.put(PICTURE, account.getPicture());
        values.put(EMAIL, account.getEmail());
        values.put(NAME, account.getName());
        values.put(VERIFIED, account.isVerified());
        values.put(NOTIFY, notify);

        return values;
    }

    public static final class FehAccountCursor extends AbstractCursor {

        private final FehAccount account;

        public FehAccountCursor(FehAccount account) {
            this.account = account;
        }

        @Override
        public int getCount() {
            return account == null ? 0 : 1;
        }

        @Override
        public String[] getColumnNames() {
            return COLUMNS.toArray(new String[5]);
        }

        @Override
        public String getString(int i) {
            if (i == SUB_IDX) {
                return account.getSub();
            } else if (i == PICTURE_IDX) {
                return account.getPicture();
            } else if (i == EMAIL_IDX) {
                return account.getEmail();
            } else if (i == NAME_IDX) {
                return account.getName();
            } else {
                throw new IllegalArgumentException(i + " not a valid index.");
            }
        }

        @Override
        public short getShort(int i) {
            if (i == VERIFIED_IDX) {
                return (short) (account.isVerified()?1:0);
            } else {
                throw new IllegalArgumentException(i + " not a valid index.");
            }
        }

        @Override
        public int getInt(int i) {
            return getShort(i);
        }

        @Override
        public long getLong(int i) {
            return getShort(i);
        }

        @Override
        public float getFloat(int i) {
            return getShort(i);
        }

        @Override
        public double getDouble(int i) {
            return getShort(i);
        }

        @Override
        public boolean isNull(int i) {
            return false;
        }
    }

}
