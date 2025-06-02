package com.example.firstappsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class CalendarPageActivity extends AppCompatActivity {

    private final HashMap<String, ArrayList<String>> moodMap = new HashMap<>();
    private final HashMap<String, String[]> quotesMap = new HashMap<>();
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        initializeQuotes();

        ImageView backPageIcon = findViewById(R.id.ic_arrow_back);
        if (backPageIcon != null) {
            // Apply the accent effect on touch for the back icon
            setMoodImageViewEffect(backPageIcon, "Back");

            backPageIcon.setOnClickListener(v -> {
                Intent intent = new Intent(CalendarPageActivity.this, NotesPageActivity.class);
                startActivity(intent);
                finish();
            });
        }

        CalendarView calendarView = findViewById(R.id.calendarView);
        ImageView happy = findViewById(R.id.ic_happy);
        ImageView inlove = findViewById(R.id.ic_inlove);
        ImageView excited = findViewById(R.id.ic_excited);
        ImageView angry = findViewById(R.id.ic_angry);
        ImageView sad = findViewById(R.id.ic_sad);
        ImageView anxious = findViewById(R.id.ic_anxious);
        ImageView btnQuote = findViewById(R.id.btn_quote);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            showMoodHistory(selectedDate);
        });

        // Add the accent effect on touch for mood icons
        setMoodImageViewEffect(happy, "Happy");
        setMoodImageViewEffect(inlove, "Inlove");
        setMoodImageViewEffect(excited, "Excited");
        setMoodImageViewEffect(angry, "Angry");
        setMoodImageViewEffect(sad, "Sad");
        setMoodImageViewEffect(anxious, "Anxious");

        // Add the accent effect for the Proverbs button
        setMoodImageViewEffect(btnQuote, "Quote");

        // Interactive Proverbs Button
        btnQuote.setOnClickListener(v -> showQuote());
    }

    private void setMood(String mood) {
        if (!selectedDate.isEmpty()) {
            String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            String moodEntry = currentTime + " - " + mood;

            // Ensure the list exists before adding
            if (!moodMap.containsKey(selectedDate)) {
                moodMap.put(selectedDate, new ArrayList<>());
            }
            moodMap.get(selectedDate).add(moodEntry);

            // Debugging log to check stored moods
            System.out.println("Moods on " + selectedDate + ": " + moodMap.get(selectedDate));

            Toast.makeText(this, "Mood on " + selectedDate + " at " + currentTime + " set to: " + mood, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select a date first!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMoodHistory(String date) {
        if (moodMap.containsKey(date)) {
            ArrayList<String> moods = moodMap.get(date);

            if (moods != null && !moods.isEmpty()) {
                StringBuilder history = new StringBuilder("Moods on " + date + ":\n");

                for (String entry : moods) {
                    history.append(entry).append("\n"); // Append each mood entry
                }

                new AlertDialog.Builder(this)
                        .setTitle("Mood History")
                        .setMessage(history.toString())
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        } else {
            Toast.makeText(this, "No mood set for " + date, Toast.LENGTH_SHORT).show();
        }
    }

    private void showQuote() {
        if (!selectedDate.isEmpty() && moodMap.containsKey(selectedDate)) {
            String lastMoodEntry = moodMap.get(selectedDate).get(moodMap.get(selectedDate).size() - 1);
            String mood = lastMoodEntry.split(" - ")[1];

            String[] quotes = quotesMap.get(mood);
            if (quotes != null && quotes.length > 0) {
                Random random = new Random();
                String randomQuote = quotes[random.nextInt(quotes.length)];

                new AlertDialog.Builder(this)
                        .setTitle("Quote for " + mood)
                        .setMessage(randomQuote)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        } else {
            Toast.makeText(this, "Please select a date and set your mood first!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeQuotes() {
        quotesMap.put("Happy", new String[] {
                "Happiness is not out there, it's in you.",
                "Smile, it’s free therapy.",
                "The happiest people don't have the best of everything, they make the best of everything.",
                "Happiness is a journey, not a destination.",
                "Do more of what makes you happy.",
                "Happiness is contagious. Spread it!",
                "Keep your face to the sunshine and you cannot see a shadow.",
                "The secret of being happy is accepting where you are in life.",
                "Enjoy the little things in life, for one day you may look back and realize they were the big things.",
                "Happiness is a choice, not a result."
        });

        quotesMap.put("Inlove", new String[] {
                "Love is not about how many days, months, or years you have been together. It's about how much you love each other every single day.",
                "Love is the flower you’ve got to let grow.",
                "To love and be loved is to feel the sun from both sides."
        });

        quotesMap.put("Excited", new String[] {
                "The only way to do great work is to love what you do.",
                "Excitement is the fuel of life!"
        });

        quotesMap.put("Angry", new String[] {
                "Holding onto anger is like drinking poison and expecting the other person to die.",
                "Anger doesn’t solve anything, it builds nothing, but it can destroy everything."
        });

        quotesMap.put("Sad", new String[] {
                "Tears come from the heart and not from the brain.",
                "Sadness flies away on the wings of time."
        });

        quotesMap.put("Anxious", new String[] {
                "Anxiety does not empty tomorrow of its sorrows, but only empties today of its strength.",
                "You don’t have to control your thoughts. You just have to stop letting them control you."
        });
    }

    private void setMoodImageViewEffect(ImageView imageView, String mood) {
        imageView.setOnClickListener(v -> {
            if (mood.equals("Quote")) {
                showQuote();
            } else if (mood.equals("Back")) {
                // Handle back button click if necessary
            } else {
                setMood(mood);
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
