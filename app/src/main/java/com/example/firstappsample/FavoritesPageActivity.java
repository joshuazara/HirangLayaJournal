package com.example.firstappsample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavoritesPageActivity extends AppCompatActivity {

    private LinearLayout favoritesContainer;
    private ImageView searchIcon, editIcon;
    private SearchView searchView;
    private JSONArray allFavoriteNotes = new JSONArray(); // to store all favorite notes for searching
    private boolean isEditMode = false; // to toggle between edit and normal mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_page);

        favoritesContainer = findViewById(R.id.favorites_container);
        searchIcon = findViewById(R.id.ic_search);
        editIcon = findViewById(R.id.ic_edit);
        searchView = findViewById(R.id.search_favorites);
        Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
        ImageView heart =findViewById(R.id.heart);
        heart.startAnimation(breathingAnimation);

        ImageView backPageIcon = findViewById(R.id.ic_arrow_back);
        if (backPageIcon != null) {
            setMoodImageViewEffect(backPageIcon, "Back");
        }

        if (searchIcon != null) {
            setMoodImageViewEffect(searchIcon, "Search");
        }

        if (editIcon != null) {
            setMoodImageViewEffect(editIcon, "Edit");
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterNotes(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterNotes(newText);
                    return true;
                }
            });
        }

        loadFavoriteNotes();
    }

    private void toggleSearchViewVisibility() {
        if (searchView.getVisibility() == View.VISIBLE) {
            searchView.setVisibility(View.GONE);
        } else {
            searchView.setVisibility(View.VISIBLE);
            searchView.requestFocus();
        }
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode; // toggle edit mode
        if (isEditMode) {
            Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Edit mode disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFavoriteNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotesData", MODE_PRIVATE);
        String notesJson = sharedPreferences.getString("notesList", "[]");

        favoritesContainer.removeAllViews();
        allFavoriteNotes = new JSONArray();  // reset the list

        try {
            JSONArray notesArray = new JSONArray(notesJson);
            for (int i = 0; i < notesArray.length(); i++) {
                JSONObject note = notesArray.getJSONObject(i);
                if (note.optBoolean("favorite", false)) {
                    allFavoriteNotes.put(note);  // store favorite notes to filter later
                    addNoteCard(note);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading favorites. Invalid data format.", Toast.LENGTH_SHORT).show();
        }
    }


    private void filterNotes(String query) {
        favoritesContainer.removeAllViews();

        try {
            for (int i = 0; i < allFavoriteNotes.length(); i++) {
                JSONObject note = allFavoriteNotes.getJSONObject(i);
                String title = note.getString("title").toLowerCase();
                if (title.contains(query.toLowerCase())) {
                    addNoteCard(note);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addNoteCard(JSONObject note) throws JSONException {
        String title = note.getString("title");
        String date = note.getString("date");

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(50, 50, 50, 50);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        card.setLayoutParams(layoutParams);
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        if (isEditMode) {
            card.setBackgroundResource(R.drawable.note_card_bg);
        } else {
            card.setBackgroundResource(R.drawable.note_card_bg_highlight);
        }

        ImageView emojiView = new ImageView(this);
        if (note.has("emojiResId")) {
            int emojiResId = note.getInt("emojiResId");
            emojiView.setImageResource(emojiResId);
            LinearLayout.LayoutParams emojiParams = new LinearLayout.LayoutParams(
                    100, 100);
            emojiParams.setMargins(0, 0, 30, 0);
            emojiView.setLayoutParams(emojiParams);
            contentLayout.addView(emojiView);}
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

        card.setOnClickListener(v -> {
            if (isEditMode) {
                String titleText = titleView.getText().toString();
                updateFavoriteStatus(titleText, false); // unmark as favorite
                favoritesContainer.removeView(card); // remove from UI
            } else {
                try {
                    Intent intent = new Intent(FavoritesPageActivity.this, EntryPageActivity.class);
                    intent.putExtra("noteTitle", note.getString("title"));
                    intent.putExtra("noteDate", note.getString("date"));
                    intent.putExtra("noteBody", note.getString("body"));
                    intent.putExtra("isFavorite", true);
                    intent.putExtra("isEditing", true);
                    intent.putExtra("emojiResId", note.optInt("emojiResId", -1));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        textLayout.addView(titleView);
        textLayout.addView(dateView);

        contentLayout.addView(textLayout);
        card.addView(contentLayout);
        favoritesContainer.addView(card);
    }

    private void updateFavoriteStatus(String title, boolean isFavorite) {
        SharedPreferences sharedPreferences = getSharedPreferences("NotesData", MODE_PRIVATE);
        String notesJson = sharedPreferences.getString("notesList", "[]");

        try {
            JSONArray notesArray = new JSONArray(notesJson);
            for (int i = 0; i < notesArray.length(); i++) {
                JSONObject note = notesArray.getJSONObject(i);
                if (note.optString("title").equals(title)) {
                    note.put("favorite", isFavorite); // update the favorite status
                    break;
                }
            }

            // Save the updated list back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("notesList", notesArray.toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log the error
        }
    }

    private void setMoodImageViewEffect(ImageView imageView, String mood) {
        imageView.setOnClickListener(v -> {
            if (mood.equals("Back")) {
                startActivity(new Intent(this, OptionPageActivity.class));
                finish();
            } else if (mood.equals("Edit")) {
                toggleEditMode();
            } else if (mood.equals("Search")) {
                toggleSearchViewVisibility();
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
