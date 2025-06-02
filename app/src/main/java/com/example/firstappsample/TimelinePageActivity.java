package com.example.firstappsample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimelinePageActivity extends AppCompatActivity {

    private LinearLayout timelineNotesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_page);

        ImageView backPageIcon = findViewById(R.id.ic_arrow_back);
        if (backPageIcon != null) {
            setMoodImageViewEffect(backPageIcon, "Back");
        }
        Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
        ImageView hourglass =findViewById(R.id.hourglass);
        hourglass.startAnimation(breathingAnimation);

        timelineNotesContainer = findViewById(R.id.favorites_container);

        // Load notes from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("NotesData", MODE_PRIVATE);
        String notesString = sharedPreferences.getString("notesList", "[]");

        try {
            JSONArray notesArray = new JSONArray(notesString);
            JSONArray filteredNotes = new JSONArray();

            // Get current time in milliseconds
            long currentTimeMillis = System.currentTimeMillis();

            // Filter notes older than 2 minutes
            for (int i = 0; i < notesArray.length(); i++) {
                JSONObject note = notesArray.getJSONObject(i);

                if (note.has("timestamp")) {
                    long noteTimestamp = note.getLong("timestamp");
                    long diffMillis = currentTimeMillis - noteTimestamp;

                    if (diffMillis >= 2 * 60 * 1000 && diffMillis < 3 * 60 * 1000) {
                        filteredNotes.put(note);
                    }
                }
            }
                // Display filtered notes
            displayNotes(filteredNotes);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error retrieving notes", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayNotes(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject note = array.getJSONObject(i);
            String title = note.getString("title");
            String date = note.getString("date");
            boolean isFavorite = note.optBoolean("favorite", false);
            JSONArray imagePositions = note.getJSONArray("imagePositions");
            String colorCodeString = note.optString("textColor", "#8B4513");
            int colorCode = Color.parseColor(colorCodeString);
            String alignmentStr = note.optString("alignment", "left");
            Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;

            if (alignmentStr.equals("center")) {
                alignment = Layout.Alignment.ALIGN_CENTER;
            } else if (alignmentStr.equals("right")) {
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
            }
            LinearLayout noteCard = new LinearLayout(this);
            noteCard.setOrientation(LinearLayout.VERTICAL);
            noteCard.setPadding(50, 50, 50, 50);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 20, 0, 20);
            noteCard.setLayoutParams(layoutParams);

            LinearLayout contentLayout = new LinearLayout(this);
            contentLayout.setOrientation(LinearLayout.HORIZONTAL);
            contentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            if (isFavorite) {
                noteCard.setBackgroundResource(R.drawable.note_card_bg_highlight);
            } else {
                noteCard.setBackgroundResource(R.drawable.note_card_bg);
            }

            // Check if the note has an emoji
            ImageView emojiView = new ImageView(this);
            if (note.has("emojiResId")) {
                int emojiResId = note.getInt("emojiResId");
                emojiView.setImageResource(emojiResId);
                LinearLayout.LayoutParams emojiParams = new LinearLayout.LayoutParams(
                        100, 100); // width, height in pixels
                emojiParams.setMargins(0, 0, 30, 0); // optional
                emojiView.setLayoutParams(emojiParams);
                contentLayout.addView(emojiView);
            }

            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setOrientation(LinearLayout.VERTICAL);

            TextView titleView = new TextView(this);
            titleView.setText(title);
            titleView.setTextSize(30);
            titleView.setTextColor(getResources().getColor(android.R.color.black));
            titleView.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView dateView = new TextView(this);
            dateView.setText(date);
            dateView.setTextSize(15);
            dateView.setTextColor(getResources().getColor(android.R.color.darker_gray));

            textLayout.addView(titleView);
            textLayout.addView(dateView);
            contentLayout.addView(textLayout);
            noteCard.addView(contentLayout);

            noteCard.setOnClickListener(v -> {
                Intent intent = new Intent(this, EntryPageActivity.class);
                intent.putExtra("noteTitle", title);
                intent.putExtra("noteDate", date);
                intent.putExtra("noteBody", note.optString("body", ""));
                intent.putExtra("emojiResId", note.optInt("emojiResId", -1));
                intent.putExtra("imagePositions", imagePositions.toString());
                intent.putExtra("alignment", alignmentStr);
                intent.putExtra("textColor", String.format("#%06X", (0xFFFFFF & colorCode)));
                intent.putExtra("sticker4", note.optInt("sticker4", -1));
                intent.putExtra("sticker5", note.optInt("sticker5", -1));
                startActivity(intent);
            });

            timelineNotesContainer.addView(noteCard);
        }
    }




    private void setMoodImageViewEffect(ImageView imageView, String mood) {
        imageView.setOnClickListener(v -> {
            if (mood.equals("Back")) {
                startActivity(new Intent(this, OptionPageActivity.class));
                finish();
            }
        });

        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setScaleX(1.1f);
                    v.setScaleY(1.1f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                    break;
            }
            return false;
        });
    }
}