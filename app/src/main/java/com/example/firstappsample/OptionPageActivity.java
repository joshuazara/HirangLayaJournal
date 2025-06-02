package com.example.firstappsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OptionPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option_page);

        Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
        Button favorites = findViewById(R.id.btn_favorites);
        favorites.startAnimation(breathingAnimation);
        favorites.setOnClickListener(v ->{
            Intent intent = new Intent(OptionPageActivity.this, FavoritesPageActivity.class);
            startActivity(intent);
            finish();
        });
        Button reflection = findViewById(R.id.btn_reflection);
        reflection.startAnimation(breathingAnimation);
        reflection.setOnClickListener(v ->{
        Intent intent = new Intent(OptionPageActivity.this, TimelinePageActivity.class);
        startActivity(intent);
        finish();
    });
        ImageView backPageIcon = findViewById(R.id.ic_arrow_back);
        if (backPageIcon != null) {
            // Apply the accent effect on touch for the back icon
            setMoodImageViewEffect(backPageIcon, "Back");

            backPageIcon.setOnClickListener(v -> {
                Intent intent = new Intent(OptionPageActivity.this, NotesPageActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
    private void setMoodImageViewEffect(ImageView imageView, String mood) {
        imageView.setOnClickListener(v -> {
             if (mood.equals("Back")) {
            }
        });

        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Scale the image on touch
                    v.setScaleX(1.1f);
                    v.setScaleY(1.1f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Reset the scale when touch is released
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                    break;
            }
            return false;
        });
    }
}