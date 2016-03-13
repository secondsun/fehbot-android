package com.feedhenry.oauth.oauth_android_app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;

import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.feedhenry.oauth.oauth_android_app.content.contract.FehMessageContract;
import com.feedhenry.oauth.oauth_android_app.content.vo.FehMessage;
import com.feedhenry.oauth.oauth_android_app.push.AccountLinkHandler;
import com.feedhenry.oauth.oauth_android_app.push.FehMessageHandler;

import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowMessages extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MessageHandler {

    private static final int LOADER = 0x100;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private MessageAdapter messageAdapter;

    private ContentObserver messagesObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getLoaderManager().initLoader(LOADER, new Bundle(), ShowMessages.this).forceLoad();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);
        ButterKnife.bind(this);

        messageAdapter = new MessageAdapter(this.getApplicationContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16;
            }
        });

        recyclerView.setAdapter(messageAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().initLoader(LOADER, new Bundle(), this).forceLoad();
        getContentResolver().registerContentObserver(FehMessageContract.URI, false, messagesObserver);
        RegistrarManager.registerMainThreadHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(messagesObserver);
        RegistrarManager.unregisterMainThreadHandler(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, FehMessageContract.URI, null , null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<FehMessage> messages = new ArrayList<>(data.getCount());
        while (data.moveToNext()) {
            messages.add(new FehMessage( data.getString(FehMessageContract.FROM_PICTURE_IDX),
                                         data.getString(FehMessageContract.FROM_NAME_IDX),
                                         data.getString(FehMessageContract.MESSAGE_IDX),
                                         new Date(data.getLong(FehMessageContract.SENT_DATE_IDX))));
        }
        data.close();
        messageAdapter.setData(messages);
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {

    }

    @Override
    public void onMessage(Context context, Bundle message) {
        String messageString = message.getString("alert");
        if (messageString.equals("{\"action\":\"link\"}")) {
            AccountLinkHandler.SaveAccountAsLinked(context, message);
        } else {
            try {
                JSONObject object = new JSONObject(messageString);
                if (object.optString("action") != null) {
                    FehMessageHandler.saveMessage(context, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError() {

    }
}
