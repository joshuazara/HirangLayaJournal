package com.example.firstappsample;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity    {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button continueButton = findViewById(R.id.continue_button);
        Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
        continueButton.startAnimation(breathingAnimation);
        continueButton.setOnClickListener(v ->{
            Intent intent = new Intent(HomePageActivity.this, NotesPageActivity.class);
            startActivity(intent);
            finish();
        });}


}