package com.example.gamenet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCardActivity extends AppCompatActivity {

    private EditText editTextCardTitle;
    private EditText editTextCardDescription;
    private EditText editTextCardGameId; // Add this
    private Button buttonSaveCard;
    private DatabaseReference cardReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        editTextCardTitle = findViewById(R.id.edit_text_card_title);
        editTextCardDescription = findViewById(R.id.edit_text_card_description);
        editTextCardGameId = findViewById(R.id.edit_text_card_game_id); // Initialize this
        buttonSaveCard = findViewById(R.id.button_save_card);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            cardReference = FirebaseDatabase.getInstance().getReference("Cards").child(userId);
        } else {
            finish();
        }

        buttonSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCard();
            }
        });
    }

    private void saveCard() {
        String title = editTextCardTitle.getText().toString().trim();
        String description = editTextCardDescription.getText().toString().trim();
        String gameId = editTextCardGameId.getText().toString().trim(); // Get the game ID

        if (title.isEmpty()) {
            editTextCardTitle.setError("Title is required");
            editTextCardTitle.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editTextCardDescription.setError("Description is required");
            editTextCardDescription.requestFocus();
            return;
        }

        if (gameId.isEmpty()) {
            editTextCardGameId.setError("Game ID is required"); // Check for Game ID
            editTextCardGameId.requestFocus();
            return;
        }

        String cardId = cardReference.push().getKey();
        Card card = new Card(title, description, gameId); // Pass the game ID

        if (cardId != null) {
            cardReference.child(cardId).setValue(card).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddCardActivity.this, "Card saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddCardActivity.this, "Failed to save card", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
