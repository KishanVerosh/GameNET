package com.example.gamenet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView imageViewProfilePhoto;
    private TextView textViewUsername;
    private TextView textViewFollowersCount;
    private Button buttonFollow;
    private RecyclerView recyclerViewCards;
    private CardAdapterNew cardAdapter;
    private List<Card> cardList;
    private DatabaseReference usersReference;
    private DatabaseReference cardReference;
    private DatabaseReference followersReference;
    private String userId;
    private String currentUserId;
    private boolean isFollowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        imageViewProfilePhoto = findViewById(R.id.image_view_profile_photo);
        textViewUsername = findViewById(R.id.text_view_username);
        textViewFollowersCount = findViewById(R.id.text_view_followers_count);
        buttonFollow = findViewById(R.id.button_follow);
        recyclerViewCards = findViewById(R.id.recycler_view_cards);

        recyclerViewCards.setLayoutManager(new LinearLayoutManager(this));
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapterNew(cardList);
        recyclerViewCards.setAdapter(cardAdapter);


        userId = getIntent().getStringExtra("userId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        cardReference = FirebaseDatabase.getInstance().getReference("Cards").child(userId);
        followersReference = FirebaseDatabase.getInstance().getReference("Followers").child(userId);


        loadUserProfile();
        loadUserCards();
        checkFollowStatus();
        loadFollowersCount();

        buttonFollow.setOnClickListener(v -> toggleFollow());


        cardAdapter.setOnItemClickListener(card -> {
            Intent intent = new Intent(UserProfileActivity.this, CardDetailActivity.class);
            intent.putExtra("cardTitle", card.getTitle());
            intent.putExtra("cardId", card.getGameId());
            intent.putExtra("cardDescription", card.getDescription());
            startActivity(intent);
        });

    }

    // Load user profile information
    private void loadUserProfile() {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    textViewUsername.setText(user.getUsername() != null ? user.getUsername() : "Username");
                    if (user.getPhotoUrl() != null) {
                        Glide.with(UserProfileActivity.this)
                                .load(user.getPhotoUrl())
                                .placeholder(R.drawable.icon_profile)
                                .into(imageViewProfilePhoto);
                    } else {
                        imageViewProfilePhoto.setImageResource(R.drawable.icon_profile);
                    }
                } else {
                    textViewUsername.setText("Username");
                    imageViewProfilePhoto.setImageResource(R.drawable.icon_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Load user's cards into RecyclerView
    private void loadUserCards() {
        cardReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Card card = snapshot.getValue(Card.class);
                    cardList.add(card);
                }
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollowStatus() {
        followersReference.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isFollowing = dataSnapshot.exists();
                updateFollowButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadFollowersCount() {
        followersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                textViewFollowersCount.setText("Followers: " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void toggleFollow() {
        if (isFollowing) {
            followersReference.child(currentUserId).removeValue();
        } else {
            followersReference.child(currentUserId).setValue(true);
        }
        isFollowing = !isFollowing;
        updateFollowButton();
    }

    private void updateFollowButton() {
        if (isFollowing) {
            buttonFollow.setText("Unfollow");
        } else {
            buttonFollow.setText("Follow");
        }
    }
}
