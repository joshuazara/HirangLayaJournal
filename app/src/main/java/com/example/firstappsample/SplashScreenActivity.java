package com.example.firstappsample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class SplashScreenActivity extends AppCompatActivity {
LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);


        lottie = findViewById(R.id.lottie);
        lottie.animate()
                .setDuration(4000)
                .setStartDelay(0)
                .start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new  Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        }, 5000);

    }


}