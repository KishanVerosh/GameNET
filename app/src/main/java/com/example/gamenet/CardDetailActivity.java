package com.example.gamenet;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CardDetailActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewId;
    private TextView textViewDescription;
    private Button buttonCopy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        textViewTitle = findViewById(R.id.text_view_title);
        textViewId = findViewById(R.id.text_view_id);
        textViewDescription = findViewById(R.id.text_view_description);
        buttonCopy = findViewById(R.id.button_copy);

        Intent intent = getIntent();
        String title = intent.getStringExtra("cardTitle");
        String id = intent.getStringExtra("cardId");
        String description = intent.getStringExtra("cardDescription");


        textViewTitle.setText(title);
        textViewId.setText(id);
        textViewDescription.setText(description);


        buttonCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Card Description", id);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(CardDetailActivity.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        });
    }
}
