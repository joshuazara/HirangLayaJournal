package com.example.firstappsample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotesPageActivity extends AppCompatActivity {

    private LinearLayout notesContainer;
    private JSONArray notesArray;
    private SearchView searchView;
    private ImageView searchIcon;
    private boolean isSelectionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_page);

        Button btnAdd = findViewById(R.id.btn_add);
        searchIcon = findViewById(R.id.ic_search);
        searchView = findViewById(R.id.search_notes);
        notesContainer = findViewById(R.id.notes_container);

        Button btnAddShadow = findViewById(R.id.btn_add_shadow);

        Button calendar = findViewById(R.id.btn_calendar);
        calendar.setOnClickListener(v ->{
            Intent intent = new Intent(NotesPageActivity.this, CalendarPageActivity.class);
            startActivity(intent);
            finish();
        });

        Button folder = findViewById(R.id.btn_folder);
        folder.setOnClickListener(v ->{
            Intent intent = new Intent(NotesPageActivity.this, OptionPageActivity.class);
            startActivity(intent);
            finish();
        });



        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.signal_pulse);
        btnAddShadow.startAnimation(pulseAnimation);
        btnAdd.setOnClickListener(v ->{
            Intent intent = new Intent(NotesPageActivity.this, EntryPageActivity.class);
            startActivity(intent);
            finish();
        });



        ImageView editIcon = findViewById(R.id.ic_edit);
        if (editIcon != null) {
            setMoodImageViewEffect(editIcon, "Edit");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("NotesData", MODE_PRIVATE);
        String notesJson = sharedPreferences.getString("notesList", "[]");

        try {
            notesArray = new JSONArray(notesJson);
            displayNotes(notesArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading notes", Toast.LENGTH_SHORT).show();
        }

        if (searchIcon != null) {
            setMoodImageViewEffect(searchIcon, "Search");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return false;
            }
        });
    }

    private void toggleSearchViewVisibility() {
        if (searchView.getVisibility() == View.GONE) {
            searchView.setVisibility(View.VISIBLE);
            searchView.animate().alpha(1).setDuration(300);
        } else {
            searchView.animate().alpha(0).setDuration(300).withEndAction(() -> searchView.setVisibility(View.GONE));
        }
    }

    private void displayNotes(JSONArray array) throws JSONException {
        notesContainer.removeAllViews();
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
            } else if (isSelectionMode) {
                noteCard.setBackgroundResource(R.drawable.note_card_bg_selectable);
            } else {
                noteCard.setBackgroundResource(R.drawable.note_card_bg);
            }


            ImageView emojiView = new ImageView(this);
            if (note.has("emojiResId")) {
                int emojiResId = note.getInt("emojiResId");
                emojiView.setImageResource(emojiResId);
                LinearLayout.LayoutParams emojiParams = new LinearLayout.LayoutParams(
                        100, 100); // width, height in pixels
                emojiParams.setMargins(0, 0, 30, 0); // optional
                emojiView.setLayoutParams(emojiParams);
                contentLayout.addView(emojiView);}

            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setOrientation(LinearLayout.VERTICAL);

            TextView titleView = new TextView(this);
            titleView.setText(title);
            titleView.setTextSize(30);
            titleView.setTextColor(Color.parseColor("#8B4513"));
            titleView.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView dateView = new TextView(this);
            dateView.setText(date);
            dateView.setTextSize(15);
            dateView.setTextColor(Color.parseColor("#8B4513"));


            textLayout.addView(titleView);
            textLayout.addView(dateView);
            contentLayout.addView(textLayout);
            noteCard.addView(contentLayout);
            ImageView imageView = new ImageView(this);
            ImageView imageView2 = new ImageView(this);
            ImageView imageView3 = new ImageView(this);
            ImageView imageView4 = new ImageView(this);
            ImageView imageView5 = new ImageView(this);
            if (imagePositions.length() >= 4) {
                JSONObject img1 = imagePositions.getJSONObject(0);
                imageView.setX((float) img1.getDouble("x"));
                imageView.setY((float) img1.getDouble("y"));

                JSONObject img2 = imagePositions.getJSONObject(1);
                imageView2.setX((float) img2.getDouble("x"));
                imageView2.setY((float) img2.getDouble("y"));

                JSONObject img3 = imagePositions.getJSONObject(2);
                imageView3.setX((float) img3.getDouble("x"));
                imageView3.setY((float) img3.getDouble("y"));

                JSONObject img4 = imagePositions.getJSONObject(3);
                imageView4.setX((float) img4.getDouble("x"));
                imageView4.setY((float) img4.getDouble("y"));

                if (imagePositions.length() > 4) {
                    JSONObject img5 = imagePositions.getJSONObject(4);
                    imageView5.setX((float) img5.getDouble("x"));
                    imageView5.setY((float) img5.getDouble("y"));
                }
            }
            int finalI = i;
            noteCard.setOnTouchListener(new View.OnTouchListener() {
                float startX = 0, endX = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = event.getX();
                            break;
                        case MotionEvent.ACTION_UP:
                            endX = event.getX();
                            if (startX - endX > 150) { // Swipe to the left to delete
                                showDeleteConfirmationDialog(note, finalI);
                            }
                            break;
                    }
                    return false;
                }
            });

            if (isSelectionMode) {
                noteCard.setOnClickListener(v -> {
                    addToFavorites(note);
                    isSelectionMode = false;
                    try {
                        displayNotes(notesArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } else {
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
            }
            notesContainer.addView(noteCard);
        }
    }
    private void showDeleteConfirmationDialog(JSONObject note, int position) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteNoteAtPosition(position);
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void deleteNoteAtPosition(int position) {
        try {
            // Remove the note at the given position
            JSONArray updatedNotesArray = new JSONArray();
            for (int i = 0; i < notesArray.length(); i++) {
                if (i != position) {
                    updatedNotesArray.put(notesArray.get(i)); // Add all notes except the deleted one
                }
            }

            notesArray = updatedNotesArray;  // Update notesArray
            SharedPreferences sharedPreferences = getSharedPreferences("NotesData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("notesList", notesArray.toString());
            editor.apply();

            // Refresh the list after deletion
            displayNotes(notesArray);
            Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
        }
    }


    private void addToFavorites(JSONObject note) {
        try {
            note.put("favorite", true);

            // Update the note in the notesArray
            for (int i = 0; i < notesArray.length(); i++) {
                JSONObject n = notesArray.getJSONObject(i);
                if (n.getString("title").equals(note.getString("title")) &&
                        n.getString("date").equals(note.getString("date"))) {
                    notesArray.put(i, note);  // update the object in the array
                    break;
                }
            }

            SharedPreferences sharedPreferences = getSharedPreferences("NotesData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("notesList", notesArray.toString());
            editor.apply();

            Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void filterNotes(String query) {
        try {
            JSONArray filteredArray = new JSONArray();
            for (int i = 0; i < notesArray.length(); i++) {
                JSONObject note = notesArray.getJSONObject(i);
                String title = note.getString("title");
                String date = note.getString("date");

                if (title.toLowerCase().contains(query.toLowerCase()) || date.contains(query)) {
                    filteredArray.put(note);
                }
            }
            displayNotes(filteredArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setMoodImageViewEffect(ImageView imageView, String mood) {
        imageView.setOnClickListener(v -> {
            if (mood.equals("Back")) {
                startActivity(new Intent(this, HomePageActivity.class));
                finish();
            } else if (mood.equals("Edit")) {
                isSelectionMode = !isSelectionMode;
                Toast.makeText(this, isSelectionMode ? "Select notes to add to favorites" : "Exited selection mode", Toast.LENGTH_SHORT).show();
                try {
                    displayNotes(notesArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
