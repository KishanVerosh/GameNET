package com.example.gamenet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;
    private Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.titleTextView.setText(post.getTitle());
        holder.descriptionTextView.setText(post.getDescription());

        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(post.getImageUrl())
                    .placeholder(R.drawable.load_image)  // Placeholder while loading
                    .error(R.drawable.load_image)  // Error image if loading fails
                    .into(holder.postImageView);
            holder.postImageView.setVisibility(View.VISIBLE);
        } else {
            holder.postImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void updatePosts(List<Post> newPostList) {
        this.postList = newPostList;
        notifyDataSetChanged();  // Notify that data has changed
    }

    public void updatePost(int position, Post updatedPost) {
        if (position >= 0 && position < postList.size()) {
            postList.set(position, updatedPost);
            notifyItemChanged(position);  // Notify that a specific item has changed
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView postImageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            postImageView = itemView.findViewById(R.id.postImageView);
        }
    }
}