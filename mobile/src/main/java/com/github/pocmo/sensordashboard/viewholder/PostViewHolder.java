package com.github.pocmo.sensordashboard.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.pocmo.sensordashboard.R;
import com.github.pocmo.sensordashboard.models.Post;
/**
 * @author: Sangwon
 */

public class PostViewHolder extends RecyclerView.ViewHolder {

    public ImageView profileView;
    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView garbageView;

    public PostViewHolder(View itemView) {
        super(itemView);

        profileView = itemView.findViewById(R.id.postAuthorPhoto);
        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.postNumStars);
        bodyView = itemView.findViewById(R.id.postBody);
        garbageView = itemView.findViewById(R.id.garbage);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, View.OnClickListener garbageClickListener) {

        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
        garbageView.setOnClickListener(garbageClickListener);
    }
}
