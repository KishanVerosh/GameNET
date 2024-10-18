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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> users;
    private final OnUserClickListener onUserClickListener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public UserAdapter(Context context, List<User> users, OnUserClickListener onUserClickListener) {
        this.context = context;
        this.users = users;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.textViewUsername.setText(user.getUsername());
        Glide.with(context)
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.icon_profile)
                .into(holder.imageViewProfilePhoto);

        holder.itemView.setOnClickListener(view -> onUserClickListener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProfilePhoto;
        TextView textViewUsername;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfilePhoto = itemView.findViewById(R.id.image_view_profile_photo);
            textViewUsername = itemView.findViewById(R.id.text_view_username);
        }
    }
}
