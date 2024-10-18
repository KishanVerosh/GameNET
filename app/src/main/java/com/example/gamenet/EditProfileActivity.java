package com.example.gamenet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProfilePhoto;
    private EditText editTextUsername;
    private Button buttonUploadPhoto;
    private Button buttonSave;
    private Button buttonLogout;
    private Uri imageUri;

    private FirebaseAuth auth;
    private DatabaseReference usersReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imageViewProfilePhoto = findViewById(R.id.image_view_profile_photo);
        editTextUsername = findViewById(R.id.edit_text_username);
        buttonUploadPhoto = findViewById(R.id.button_upload_photo);
        buttonSave = findViewById(R.id.button_save);
        buttonLogout = findViewById(R.id.button_logout);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            storageReference = FirebaseStorage.getInstance().getReference("ProfileImages").child(userId);
        }

        buttonUploadPhoto.setOnClickListener(v -> openFileChooser());

        buttonSave.setOnClickListener(v -> saveProfile(editTextUsername.getText().toString().trim()));

        buttonLogout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewProfilePhoto.setImageURI(imageUri);
        }
    }

    private void saveProfile(@Nullable String username) {
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child("profile.jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String photoUrl = uri.toString();
                        updateProfile(username, photoUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            updateProfile(username, null);
        }
    }

    private void updateProfile(@Nullable String username, @Nullable String photoUrl) {
        Map<String, Object> updates = new HashMap<>();
        if (username != null && !username.isEmpty()) {
            updates.put("username", username);
        }
        if (photoUrl != null) {
            updates.put("photoUrl", photoUrl);
        }
        usersReference.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
