package org.aerogear.oauth.oauth_android_app.content.vo;

import android.content.Context;

import org.aerogear.oauth.oauth_android_app.content.contract.FehAccountContract;
import org.aerogear.oauth.oauth_android_app.content.contract.FehMessageContract;

import java.util.Date;

/**
 * Created by summers on 3/16/16.
 */
public class FehMessage {
    private final String fromPicture;
    private final String fromName;
    private final String message;
    private final Date sentDate;

    public FehMessage(String fromPicture, String fromName, String message, Date sentDate) {
        this.fromPicture = fromPicture;
        this.fromName = fromName;
        this.message = message;
        this.sentDate = sentDate;
    }

    public String getFromPicture() {
        return fromPicture;
    }

    public String getFromName() {
        return fromName;
    }

    public String getMessage() {
        return message;
    }

    /**
     * SentDate is the date the message was sent.  It is stored as UTC.
     * @return the sentDate
     */
    public Date getSentDate() {
        return sentDate;
    }

    public void save(Context context) {
        context.getContentResolver().insert(FehMessageContract.URI,FehMessageContract.valueize(this,true));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FehMessage that = (FehMessage) o;

        if (fromPicture != null ? !fromPicture.equals(that.fromPicture) : that.fromPicture != null)
            return false;
        if (!fromName.equals(that.fromName)) return false;
        if (!message.equals(that.message)) return false;
        return sentDate.equals(that.sentDate);

    }

    @Override
    public int hashCode() {
        int result = fromPicture != null ? fromPicture.hashCode() : 0;
        result = 31 * result + fromName.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + sentDate.hashCode();
        return result;
    }
}
