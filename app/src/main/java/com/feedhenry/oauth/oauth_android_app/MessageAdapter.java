package com.feedhenry.oauth.oauth_android_app;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feedhenry.oauth.oauth_android_app.content.vo.FehMessage;
import com.feedhenry.oauth.oauth_android_app.util.ImageFromNameMaker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by summers on 3/16/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    private final Context context;
    private List<FehMessage> data = new ArrayList<>();

    public MessageAdapter(Context applicationContext) {
        this.context = applicationContext;
        setHasStableIds(true);
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View card = inflater.inflate(R.layout.message_card_layout, null);

        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        FehMessage message = data.get(position);
        holder.message.setText(message.getMessage());
        holder.from.setText(message.getFromName());
        if (message.getFromPicture() != null && !message.getFromPicture().isEmpty()) {
            Picasso.with(context).load(message.getFromPicture()).into(holder.image);
        } else {
            holder.image.setImageDrawable(ImageFromNameMaker.makeImage(context, message.getFromName()));
        }
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<FehMessage> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView message;
        TextView from;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            message = (TextView) itemView.findViewById(R.id.message);
            from = (TextView) itemView.findViewById(R.id.from);

        }
    }

}
