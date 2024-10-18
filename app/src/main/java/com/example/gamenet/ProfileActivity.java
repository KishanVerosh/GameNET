package com.example.gamenet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends Fragment {

    private TextView textViewUsername;
    private TextView textViewProfile;
    private TextView textViewFollowCount;
    private ImageView imageViewProfilePhoto; // Add this
    private RecyclerView recyclerViewCards;
    private Button editprofile; // Add this
    private FloatingActionButton fabAddCard;
    private FirebaseAuth auth;
    private DatabaseReference usersReference;
    private DatabaseReference cardReference;
    private DatabaseReference followersReference;

    private List<Card> cardList;
    private CardAdapter cardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        textViewUsername = view.findViewById(R.id.text_view_username);
        textViewProfile = view.findViewById(R.id.text_view_profile);
        textViewFollowCount = view.findViewById(R.id.text_view_follow_count);
        imageViewProfilePhoto = view.findViewById(R.id.image_view_profile_photo); // Initialize
        recyclerViewCards = view.findViewById(R.id.recycler_view_cards);
        fabAddCard = view.findViewById(R.id.fab_add_card);
        editprofile = view.findViewById(R.id.button_edit_profile); // Initialize

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            cardReference = FirebaseDatabase.getInstance().getReference("Cards").child(userId);
            followersReference = FirebaseDatabase.getInstance().getReference("Followers").child(userId);

            loadUserProfile();
            loadUserCards();
            loadFollowCount();
        } else {
            getActivity().finish();
        }

        recyclerViewCards.setLayoutManager(new LinearLayoutManager(getContext()));
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList);
        recyclerViewCards.setAdapter(cardAdapter);

        fabAddCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCardActivity.class);
            startActivity(intent);
        });

        editprofile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserProfile() {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    textViewUsername.setText(user.getUsername());
                    textViewProfile.setText(user.getUsername());

                    // Load profile photo
                    String photoUrl = user.getPhotoUrl();
                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(photoUrl)
                                .placeholder(R.drawable.icon_profile) // Optional placeholder
                                .into(imageViewProfilePhoto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

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

    private void loadFollowCount() {
        followersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long followCount = dataSnapshot.getChildrenCount();
                textViewFollowCount.setText("Followers: " + followCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
