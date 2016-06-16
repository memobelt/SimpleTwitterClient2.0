package io.intrepid.simpletwitterclient20.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.intrepid.simpletwitterclient20.R;
import twitter4j.Status;
import twitter4j.User;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private static final String TAG = TweetAdapter.class.getSimpleName();

    List<Status> tweets;
    Context context;

    public TweetAdapter(List<Status> tweets, Context context) {
        this.tweets = tweets;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cards_feed, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status status = tweets.get(position);
        User user = status.getUser();
        holder.tvName.setText(user.getName());
        holder.tvScreenName.setText(user.getScreenName());
        Glide.with(context).load(user.getProfileImageURL()).into(holder.ivProfile);
        holder.tvTweetText.setText(status.getText());
        Linkify.addLinks(holder.tvTweetText, Linkify.WEB_URLS);
        holder.itemView.setTag(status);
        setColors(holder, user);
    }

    public void setColors(ViewHolder holder, User user) {
        int textColor = Color.parseColor("#" + user.getProfileTextColor());
        try {
            holder.tvName.setTextColor(textColor);
            holder.tvScreenName.setTextColor(textColor);
            holder.tvTweetText.setTextColor(textColor);
            holder.rlBackgroundCards.setBackgroundColor(Color.parseColor("#" +
                    user.getProfileSidebarFillColor()));
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvScreenName;
        TextView tvTweetText;
        ImageView ivProfile;
        RelativeLayout rlBackgroundCards;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvScreenName = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tvTweetText = (TextView) itemView.findViewById(R.id.tv_tweet_text);
            tvTweetText.setMovementMethod(LinkMovementMethod.getInstance());
            ivProfile = (ImageView) itemView.findViewById(R.id.iv_profile);
            rlBackgroundCards = (RelativeLayout) itemView.findViewById(R.id.background_cards);
        }
    }
}
