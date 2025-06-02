            package com.example.firstappsample;

            import android.content.Intent;
            import android.content.SharedPreferences;
            import android.content.pm.PackageManager;
            import android.graphics.Bitmap;
            import android.graphics.Color;
            import android.graphics.Typeface;
            import android.net.Uri;
            import android.os.Build;
            import android.os.Bundle;
            import android.provider.MediaStore;
            import android.text.Editable;
            import android.text.Html;
            import android.text.Layout;
            import android.text.Spannable;
            import android.text.SpannableString;
            import android.text.TextWatcher;
            import android.text.style.AlignmentSpan;
            import android.text.style.ForegroundColorSpan;
            import android.text.style.ImageSpan;
            import android.text.style.StyleSpan;
            import android.text.style.UnderlineSpan;
            import android.util.Log;
            import android.view.GestureDetector;
            import android.view.Gravity;
            import android.view.MotionEvent;
            import android.view.View;
            import android.view.ViewGroup;
            import android.view.ViewTreeObserver;
            import android.widget.Button;
            import android.widget.EditText;
            import android.widget.ImageView;
            import android.widget.LinearLayout;
            import android.widget.RelativeLayout;
            import android.widget.ScrollView;
            import android.widget.Toast;
            import android.Manifest;
            import android.os.Build;
            import android.content.pm.PackageManager;

            import androidx.appcompat.app.AlertDialog;
            import androidx.core.app.ActivityCompat;
            import androidx.core.content.ContextCompat;

            import androidx.appcompat.app.AppCompatActivity;
            import androidx.core.app.ActivityCompat;
            import androidx.core.content.ContextCompat;
            import androidx.annotation.NonNull;

            import java.io.IOException;
            import java.text.SimpleDateFormat;
            import java.util.ArrayList;
            import java.util.Date;
            import java.util.List;
            import android.net.Uri;

            import com.bumptech.glide.Glide;
            import com.example.firstappsample.dbhelper.DatabaseHelper;

            import org.json.JSONArray;
            import org.json.JSONException;
            import org.json.JSONObject;

            import java.util.ArrayList;
            import java.util.List;
            import java.util.Locale;

            public class EntryPageActivity extends AppCompatActivity {
                private List<Uri> selectedImages = new ArrayList<>();

                JSONObject noteObject = null;
                private static final int REQUEST_PICK_IMAGE = 102;

                private static final int REQUEST_GALLERY_PERMISSION = 101;
                EditText noteTitle, noteBody;
                private final int TITLE_MAX_LENGTH = 18;
                boolean isEditingExistingNote = false;
                String originalTitle = null;
                private int currentSelectedEmoji = -1;
                private int selectedDrawableForImage = -1;

                private int seocndSelectedDrawableForImage = -1;
                int colorCode;
                private Layout.Alignment selectedAlignment = Layout.Alignment.ALIGN_NORMAL;

                ImageView imageView, imageView2, imageView3, imageView4, imageView5;


                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_entry_page);

                    noteTitle = findViewById(R.id.note_title);
                    noteBody = findViewById(R.id.note_body);
                    ImageView backPageIcon = findViewById(R.id.ic_arrow_back);
                    Button checkIcon = findViewById(R.id.ic_check);
                    View dimBg = findViewById(R.id.dim_background);
                    LinearLayout moodGallery = findViewById(R.id.mood_gallery);
                    LinearLayout layoutEdit = findViewById(R.id.layout_edit);
                    ImageView emojiDefault = findViewById(R.id.emoji_default);

                    ImageView emojiHappy = findViewById(R.id.emoji_happy);
                    ImageView emojiSad = findViewById(R.id.emoji_sad);
                    ImageView emojiLove = findViewById(R.id.emoji_love);
                    ImageView emojiShock = findViewById(R.id.emoji_shock);
                    ImageView emojiCry = findViewById(R.id.emoji_cry);
                    ImageView emojiAngry = findViewById(R.id.embed_angry);

                    dimBg.setVisibility(View.VISIBLE);
                    moodGallery.setVisibility(View.VISIBLE);
                    checkIcon.setVisibility(View.GONE);
                    layoutEdit.setVisibility(View.GONE);
                    noteBody.setVisibility(View.GONE);
                    noteTitle.setVisibility(View.GONE);

                    emojiHappy.setOnTouchListener((v, event) -> {
                        Toast.makeText(getApplicationContext(), "Feeling Happy! \uD83D\uDE0A", Toast.LENGTH_SHORT).show();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                                return true;
                            case MotionEvent.ACTION_UP:
                                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                                emojiDefault.setImageResource(R.drawable.emoji_happy);
                                currentSelectedEmoji = R.drawable.emoji_happy;

                                return true;
                            default:
                                return false;
                        }
                    });

                    emojiSad.setOnTouchListener((v, event) -> {
                        Toast.makeText(getApplicationContext(), "Feeling Down. \uD83D\uDE1E", Toast.LENGTH_SHORT).show();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                                return true;
                            case MotionEvent.ACTION_UP:
                                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                                emojiDefault.setImageResource(R.drawable.emoji_sad);
                                currentSelectedEmoji = R.drawable.emoji_sad;

                                return true;
                            default:
                                return false;
                        }
                    });

                    emojiLove.setOnTouchListener((v, event) -> {
                        Toast.makeText(getApplicationContext(), "Feeling Love!  \uD83D\uDC96", Toast.LENGTH_SHORT).show();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                                return true;
                            case MotionEvent.ACTION_UP:
                                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                                emojiDefault.setImageResource(R.drawable.emoji_love);
                                currentSelectedEmoji = R.drawable.emoji_love;

                                return true;
                            default:
                                return false;
                        }
                    });

                    emojiShock.setOnTouchListener((v, event) -> {
                        Toast.makeText(getApplicationContext(), "Feeling Surprise! \uD83D\uDE32", Toast.LENGTH_SHORT).show();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                                return true;
                            case MotionEvent.ACTION_UP:
                                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                                emojiDefault.setImageResource(R.drawable.emoji_shock);
                                currentSelectedEmoji = R.drawable.emoji_shock;

                                return true;
                            default:
                                return false;
                        }
                    });

                    emojiCry.setOnTouchListener((v, event) -> {
                        Toast.makeText(getApplicationContext(), "Feeling Tearful. \uD83D\uDE22", Toast.LENGTH_SHORT).show();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                                return true;
                            case MotionEvent.ACTION_UP:
                                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                                emojiDefault.setImageResource(R.drawable.emoji_cy);
                                currentSelectedEmoji = R.drawable.emoji_cy;

                                return true;
                            default:
                                return false;
                        }
                    });

                    emojiAngry.setOnTouchListener((v, event) -> {
                        Toast.makeText(getApplicationContext(), "Feeling Angry! \uD83D\uDE20", Toast.LENGTH_SHORT).show();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                                return true;
                            case MotionEvent.ACTION_UP:
                                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                                emojiDefault.setImageResource(R.drawable.emoji_angry);
                                currentSelectedEmoji = R.drawable.emoji_angry;

                                return true;
                            default:
                                return false;
                        }
                    });


                    dimBg.setOnClickListener(v -> {
                        dimBg.setVisibility(View.GONE);
                        moodGallery.setVisibility(View.GONE);
                        layoutEdit.setVisibility(View.VISIBLE);
                        checkIcon.setVisibility(View.VISIBLE);
                        noteTitle.setVisibility(View.VISIBLE);
                        noteBody.setVisibility(View.VISIBLE);
                        imageView4.setVisibility((View.VISIBLE));
                        imageView5.setVisibility((View.VISIBLE));
                    });

                    emojiDefault.setOnClickListener(v -> {
                        dimBg.setVisibility(View.VISIBLE);
                        moodGallery.setVisibility(View.VISIBLE);
                        layoutEdit.setVisibility(View.GONE);
                        checkIcon.setVisibility(View.GONE);
                        noteTitle.setVisibility(View.GONE);
                        noteBody.setVisibility(View.GONE);
                        imageView4.setVisibility((View.GONE));
                        imageView5.setVisibility((View.GONE));
                    });

                    ImageView editImage = findViewById(R.id.edit_image);
                    editImage.setOnClickListener(v -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                                    == PackageManager.PERMISSION_GRANTED) {
                                openGallery();
                            } else {
                                ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                                        REQUEST_GALLERY_PERMISSION);
                            }
                        } else {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                openGallery();
                            } else {
                                ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_GALLERY_PERMISSION);
                            }
                        }
                    });

                    View.OnTouchListener moveTouchListener = createSmoothMoveTouchListener();
                    imageView = findViewById(R.id.imageView);
                    imageView2 = findViewById(R.id.imageView2);
                    imageView3 = findViewById(R.id.imageView3);
                    imageView4 = findViewById(R.id.imageView4);
                    imageView5 = findViewById(R.id.imageView5);


                    ImageView kiss = findViewById(R.id.kiss);
                    ImageView bunny = findViewById(R.id.bunny);
                    ImageView sunglasses = findViewById(R.id.sunglasses);
                    ImageView car = findViewById(R.id.car);

                    kiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageView4.setImageResource(R.drawable.kiss);
                            selectedDrawableForImage = R.drawable.kiss;
                        }
                    });

                    sunglasses.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageView4.setImageResource(R.drawable.sunglasses);
                            selectedDrawableForImage = R.drawable.sunglasses;
                        }
                    });

                    bunny.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageView5.setImageResource(R.drawable.bunny);
                            seocndSelectedDrawableForImage = R.drawable.bunny;
                        }
                    });

                    car.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageView5.setImageResource(R.drawable.car);
                            seocndSelectedDrawableForImage = R.drawable.sunglasses;
                        }
                    });

                    imageView.setOnTouchListener(moveTouchListener);
                    imageView2.setOnTouchListener(moveTouchListener);
                    imageView3.setOnTouchListener(moveTouchListener);
                    imageView4.setOnTouchListener(moveTouchListener);
                    imageView5.setOnTouchListener(moveTouchListener);

                    noteBody.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            imageView.setTranslationY(imageView.getTranslationY() - (scrollY - oldScrollY));
                            imageView2.setTranslationY(imageView2.getTranslationY() - (scrollY - oldScrollY));
                            imageView3.setTranslationY(imageView3.getTranslationY() - (scrollY - oldScrollY));
                            imageView4.setTranslationY(imageView4.getTranslationY() - (scrollY - oldScrollY));
                            imageView5.setTranslationY(imageView5.getTranslationY() - (scrollY - oldScrollY));
                        }
                    });


                    noteTitle.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() > TITLE_MAX_LENGTH) {
                                Toast.makeText(EntryPageActivity.this, "Maximum 18 characters allowed", Toast.LENGTH_SHORT).show();
                                noteTitle.setText(s.subSequence(0, TITLE_MAX_LENGTH));
                                noteTitle.setSelection(TITLE_MAX_LENGTH);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    ImageView edittxt = findViewById(R.id.edit_text);
                    LinearLayout textedit = findViewById(R.id.text_edit);
                    edittxt.setOnClickListener(v -> {
                        textedit.setVisibility(View.VISIBLE);
                    });

                    ImageView justifyAlignment = findViewById(R.id.justify_alignment);
                    ImageView leftAlignment = findViewById(R.id.left_alignment);
                    ImageView rightAlignment = findViewById(R.id.right_alignment);

                    justifyAlignment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedAlignment = Layout.Alignment.ALIGN_CENTER;
                            applyAlignmentToSelection(noteBody, selectedAlignment);
                        }
                    });

                    leftAlignment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedAlignment = Layout.Alignment.ALIGN_NORMAL;
                            applyAlignmentToSelection(noteBody, selectedAlignment);
                        }
                    });

                    rightAlignment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedAlignment = Layout.Alignment.ALIGN_OPPOSITE;
                            applyAlignmentToSelection(noteBody, selectedAlignment);
                        }
                    });
                    ImageView bold = findViewById(R.id.bold);
                    ImageView italic = findViewById(R.id.italic);
                    ImageView underline = findViewById(R.id.underline);

                    bold.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            applyTextStyle(noteBody, Typeface.BOLD);
                        }
                    });

                    italic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            applyTextStyle(noteBody, Typeface.ITALIC);
                        }
                    });

                    underline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            applyTextUnderline(noteBody);
                        }
                    });
                    ImageView black = findViewById(R.id.black);
                    ImageView brown = findViewById(R.id.brown);
                    ImageView pink = findViewById(R.id.pink);

                    black.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            colorCode = Color.BLACK;
                            applyTextColor(noteBody, colorCode);
                        }
                    });

                    brown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            colorCode = 0xFF8B4513;
                            applyTextColor(noteBody, colorCode);
                        }
                    });

                    pink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            colorCode = 0xFFFF69B4;
                            applyTextColor(noteBody, colorCode);
                        }
                    });

                    ImageView editList = findViewById(R.id.edit_list);
                    editList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            insertBulletAtSelection(noteBody);
                        }
                    });

                    ImageView editStar = findViewById(R.id.edit_star);
                    LinearLayout editSticker = findViewById(R.id.layout_sticker);
                    editStar.setOnClickListener(v -> {
                        editSticker.setVisibility(View.VISIBLE);
                    });





                    Intent intent = getIntent();
                    if (intent != null && intent.hasExtra("noteTitle")) {
                        String title = intent.getStringExtra("noteTitle");
                        String body = intent.getStringExtra("noteBody");
                        int emojiResId = intent.getIntExtra("emojiResId", -1);
                        int sticker4 = intent.getIntExtra("sticker4", -1);
                        int sticker5 = intent.getIntExtra("sticker5", -1);
                        String imagePositionsString = intent.getStringExtra("imagePositions");
                        String alignmentStr = getIntent().getStringExtra("alignment");
                        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
                        String colorCodeString = intent.getStringExtra("textColor");


                        if ("center".equals(alignmentStr)) {
                            alignment = Layout.Alignment.ALIGN_CENTER;
                        } else if ("right".equals(alignmentStr)) {
                            alignment = Layout.Alignment.ALIGN_OPPOSITE;
                        }

                        noteTitle.setText(title);
                        noteBody.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY));
                        applyAlignmentToSelection(noteBody, alignment);
                        if (colorCodeString != null) {
                            int colorCode = Color.parseColor(colorCodeString);
                            noteBody.setTextColor(colorCode);
                        }
                        if (imagePositionsString != null) {
                            try {
                                JSONArray imagePositions = new JSONArray(imagePositionsString);

                                if (imagePositions.length() >= 1) {
                                    JSONObject pos1 = imagePositions.getJSONObject(0);
                                    imageView.setX((float) pos1.getDouble("x"));
                                    imageView.setY((float) pos1.getDouble("y"));
                                }
                                if (imagePositions.length() >= 2) {
                                    JSONObject pos2 = imagePositions.getJSONObject(1);
                                    imageView2.setX((float) pos2.getDouble("x"));
                                    imageView2.setY((float) pos2.getDouble("y"));
                                }
                                if (imagePositions.length() >= 3) {
                                    JSONObject pos3 = imagePositions.getJSONObject(2);
                                    imageView3.setX((float) pos3.getDouble("x"));
                                    imageView3.setY((float) pos3.getDouble("y"));
                                }
                                if (imagePositions.length() >= 4) {
                                    JSONObject pos4 = imagePositions.getJSONObject(3);
                                    imageView4.setX((float) pos4.getDouble("x"));
                                    imageView4.setY((float) pos4.getDouble("y"));
                                }
                                if (imagePositions.length() >= 5) {
                                    JSONObject pos5 = imagePositions.getJSONObject(4);
                                    imageView5.setX((float) pos5.getDouble("x"));
                                    imageView5.setY((float) pos5.getDouble("y"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("ImagePosError", "Invalid imagePositions JSON");
                            }
                        }
                        if (emojiResId != -1) {
                            emojiDefault.setImageResource(emojiResId);}
                        if (sticker4 != -1) {
                            imageView4.setImageResource(sticker4);}
                        if (sticker5 != -1) {
                            imageView5.setImageResource(sticker5);}

                        isEditingExistingNote = true;
                        originalTitle = title;
                    }


                    noteBody.setOnClickListener(v -> {
                        textedit.setVisibility(View.GONE);
                        editSticker.setVisibility(View.GONE);
                    });

                    if (backPageIcon != null) {
                        setMoodImageViewEffect(backPageIcon, "Back");
                    }

                    if (checkIcon != null) {
                        checkIcon.setOnClickListener(v -> {

                            String title = noteTitle.getText().toString().trim();
                            String body = noteBody.getText().toString().trim();
                            String htmlBody = Html.toHtml(noteBody.getText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);

                            JSONArray imageUris = new JSONArray();
                            if (title.isEmpty() || title.equals(getString(R.string.title))) {
                                try {
                                    if (noteObject != null && noteObject.has("imagePositions")) {
                                        JSONArray imagePositions = noteObject.getJSONArray("imagePositions");
                                        if (imagePositions.length() >= 3) {
                                            JSONObject img1 = imagePositions.getJSONObject(0);
                                            imageView.setX((float) img1.getDouble("x"));
                                            imageView.setY((float) img1.getDouble("y"));

                                            JSONObject img2 = imagePositions.getJSONObject(1);
                                            imageView2.setX((float) img2.getDouble("x"));
                                            imageView2.setY((float) img2.getDouble("y"));

                                            JSONObject img3 = imagePositions.getJSONObject(2);
                                            imageView3.setX((float) img3.getDouble("x"));
                                            imageView3.setY((float) img3.getDouble("y"));
                                            if (imagePositions.length() >= 5) {
                                                JSONObject img4 = imagePositions.getJSONObject(3);
                                                imageView4.setX((float) img4.getDouble("x"));
                                                imageView4.setY((float) img4.getDouble("y"));

                                                JSONObject img5 = imagePositions.getJSONObject(4);
                                                imageView5.setX((float) img5.getDouble("x"));
                                                imageView5.setY((float) img5.getDouble("y"));
                                            }
                                        }
                                    } else {
                                        Log.e("ImagePositionError", "Image positions are not available or noteObject is null.");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("ImagePositionError", "Error parsing image positions", e);
                                }

                                Toast.makeText(EntryPageActivity.this, "Please provide a valid title.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                    .format(new Date());

                            SharedPreferences sharedPreferences = getSharedPreferences("NotesData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String existingNotesJson = sharedPreferences.getString("notesList", "[]");
                            String existingFavoritesJson = sharedPreferences.getString("favoritesList", "[]");

                            try {
                                JSONArray notesArray = new JSONArray(existingNotesJson);
                                JSONArray updatedNotesArray = new JSONArray();
                                JSONArray updatedFavoritesArray = new JSONArray();

                                for (int i = 0; i < notesArray.length(); i++) {
                                    JSONObject note = notesArray.getJSONObject(i);
                                    if (isEditingExistingNote && note.getString("title").equals(originalTitle)) {
                                        continue;
                                    }
                                    updatedNotesArray.put(note);
                                }

                                JSONObject newNote = new JSONObject();
                                newNote.put("title", title);
                                newNote.put("body", body);
                                newNote.put("date", currentDate);
                                newNote.put("timestamp", System.currentTimeMillis());
                                newNote.put("body", htmlBody);
                                newNote.put("emojiResId", currentSelectedEmoji);
                                String alignmentString = "left"; // default
                                if (selectedAlignment == Layout.Alignment.ALIGN_CENTER) {
                                    alignmentString = "center";
                                } else if (selectedAlignment == Layout.Alignment.ALIGN_OPPOSITE) {
                                    alignmentString = "right";
                                }
                                newNote.put("alignment", alignmentString);
                                for (Uri uri : selectedImages) {
                                    imageUris.put(uri.toString());
                                }
                                newNote.put("image1Uri", imageUris);

                                newNote.put("textColor", String.format("#%06X", (0xFFFFFF & colorCode)));
                                newNote.put("sticker4", selectedDrawableForImage);
                                newNote.put("sticker5", seocndSelectedDrawableForImage);

                                DatabaseHelper dbHelper = new DatabaseHelper(EntryPageActivity.this);
                                boolean isInserted = dbHelper.insertNote(title, htmlBody, currentDate);

                                if (!isInserted) {
                                    Toast.makeText(EntryPageActivity.this, "Failed to save note in local database.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("SQLiteSave", "Note saved successfully to SQLite.");
                                }


                                JSONArray imagePositions = new JSONArray();
                                addImagePosition(imagePositions, imageView);
                                addImagePosition(imagePositions, imageView2);
                                addImagePosition(imagePositions, imageView3);

                                addImagePosition(imagePositions, imageView4);
                                addImagePosition(imagePositions, imageView5);
                                if (imagePositions != null && imagePositions.length() > 0) {
                                    newNote.put("imagePositions", imagePositions);
                                }

                                JSONArray finalNotesArray = new JSONArray();
                                finalNotesArray.put(newNote);

                                for (int i = 0; i < updatedNotesArray.length(); i++) {
                                    finalNotesArray.put(updatedNotesArray.getJSONObject(i));
                                }

                                boolean isInFavorites = false;
                                if (existingFavoritesJson != null && !existingFavoritesJson.isEmpty()) {
                                    JSONArray favoritesArray = new JSONArray(existingFavoritesJson);
                                    for (int i = 0; i < favoritesArray.length(); i++) {
                                        JSONObject favNote = favoritesArray.getJSONObject(i);
                                        if (isEditingExistingNote && favNote.getString("title").equals(originalTitle)) {
                                            favNote.put("title", title);
                                            favNote.put("body", body);
                                            favNote.put("date", currentDate);
                                            favNote.put("timestamp", System.currentTimeMillis());
                                            favNote.put("emojiResId", currentSelectedEmoji);
                                            if (selectedAlignment == Layout.Alignment.ALIGN_CENTER) {
                                                alignmentString = "center";
                                            } else if (selectedAlignment == Layout.Alignment.ALIGN_OPPOSITE) {
                                                alignmentString = "right";
                                            }
                                            favNote.put("alignment", alignmentString);
                                            for (Uri uri : selectedImages) {
                                                imageUris.put(uri.toString());
                                            }
                                            favNote.put("image1Uri", imageUris);

                                            favNote.put("textColor", String.format("#%06X", (0xFFFFFF & colorCode)));
                                            favNote.put("sticker4", selectedDrawableForImage);
                                            favNote.put("sticker5", seocndSelectedDrawableForImage);

                                            addImagePosition(imagePositions, imageView);
                                            addImagePosition(imagePositions, imageView2);
                                            addImagePosition(imagePositions, imageView3);

                                            addImagePosition(imagePositions, imageView4);
                                            addImagePosition(imagePositions, imageView5);
                                            if (imagePositions != null && imagePositions.length() > 0) {
                                                favNote.put("imagePosition" +
                                                        "s", imagePositions);
                                            }
                                            isInFavorites = true;
                                        }
                                        updatedFavoritesArray.put(favNote);
                                    }
                                }

                                if (isInFavorites) {
                                    editor.putString("favoritesList", updatedFavoritesArray.toString());
                                }

                                editor.putString("notesList", finalNotesArray.toString());
                                editor.apply();


                                Toast.makeText(EntryPageActivity.this, "Note Saved! \uD83D\uDCDD", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EntryPageActivity.this, NotesPageActivity.class));
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("SaveNoteError", "JSONException occurred", e);
                                Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                private void addImagePosition(JSONArray imagePositions, ImageView imageView) throws JSONException {
                    JSONObject imagePosition = new JSONObject();
                    try {
                        imagePosition.put("x", imageView.getX());
                        imagePosition.put("y", imageView.getY());
                        imagePositions.put(imagePosition);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                private void applyTextColor(EditText editText, int color) {
                    int start = editText.getSelectionStart();
                    int end = editText.getSelectionEnd();

                    if (start == end) {
                        editText.setTextColor(color);
                    } else {
                        Spannable spannable = new SpannableString(editText.getText());
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                        spannable.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannable);
                        editText.setSelection(start, end); // Para hindi mawala highlight
                    }
                }
                private void insertBulletAtSelection(EditText editText) {
                    int start = editText.getSelectionStart();
                    int end = editText.getSelectionEnd();

                    Editable text = editText.getText();

                    if (start == end) {
                        // Walang naka-highlight, insert bullet at current cursor
                        text.insert(start, "• ");
                    } else {
                        // May naka-highlight, lagyan natin ng bullet bawat line
                        String selectedText = text.subSequence(start, end).toString();
                        String[] lines = selectedText.split("\n");

                        StringBuilder bulletText = new StringBuilder();
                        for (String line : lines) {
                            if (!line.trim().isEmpty()) {
                                bulletText.append("• ").append(line).append("\n");
                            }
                        }

                        text.replace(start, end, bulletText.toString());
                    }
                }
                private void setMoodImageViewEffect(ImageView imageView, String mood) {
                    imageView.setOnClickListener(v -> {
                        if (mood.equals("Back")) {
                            startActivity(new Intent(this, NotesPageActivity.class));
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
                private View.OnTouchListener createSmoothMoveTouchListener() {
                    return new View.OnTouchListener() {
                        float dX, dY;
                        boolean isDragging = false;

                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            switch (event.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                    dX = event.getRawX() - view.getX();
                                    dY = event.getRawY() - view.getY();
                                    isDragging = true;
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    if (isDragging) {
                                        float newX = event.getRawX() - dX;
                                        float newY = event.getRawY() - dY;

                                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                                        layoutParams.leftMargin = (int) newX;
                                        layoutParams.topMargin = (int) newY;
                                        view.setLayoutParams(layoutParams);
                                    }
                                    break;

                                case MotionEvent.ACTION_UP:
                                    isDragging = false;
                                    break;
                            }
                            return true;
                        }
                    };
                }
                private void applyTextStyle(EditText editText, int style) {
                    int start = editText.getSelectionStart();
                    int end = editText.getSelectionEnd();

                    if (start != end) { // Text is selected
                        Spannable spannable = new SpannableString(editText.getText());
                        StyleSpan styleSpan = new StyleSpan(style); // Bold or Italic
                        spannable.setSpan(styleSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannable);
                    } else { // No text is selected, apply to whole text
                        Spannable spannable = new SpannableString(editText.getText());
                        StyleSpan styleSpan = new StyleSpan(style); // Bold or Italic
                        spannable.setSpan(styleSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannable);
                    }
                }

                private void applyTextUnderline(EditText editText) {
                    int start = editText.getSelectionStart();
                    int end = editText.getSelectionEnd();

                    if (start != end) { // Text is selected
                        Spannable spannable = new SpannableString(editText.getText());
                        UnderlineSpan underlineSpan = new UnderlineSpan();
                        spannable.setSpan(underlineSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannable);
                    } else { // No text is selected, apply to whole text
                        Spannable spannable = new SpannableString(editText.getText());
                        UnderlineSpan underlineSpan = new UnderlineSpan();
                        spannable.setSpan(underlineSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannable);
                    }
                }


                private void applyAlignmentToSelection(EditText editText, Layout.Alignment alignment) {
                    int start = editText.getSelectionStart();
                    int end = editText.getSelectionEnd();

                    if (start == end) {
                        editText.setGravity(
                                alignment == Layout.Alignment.ALIGN_CENTER ? Gravity.CENTER_HORIZONTAL :
                                        alignment == Layout.Alignment.ALIGN_NORMAL ? Gravity.START :
                                                Gravity.END
                        );
                    } else {
                        Spannable spannable = new SpannableString(editText.getText());
                        AlignmentSpan.Standard span = new AlignmentSpan.Standard(alignment);
                        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannable);
                    }
                }


                private void openGallery() {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, EntryPageActivity.REQUEST_PICK_IMAGE);
                        }
                @Override
                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    if (requestCode == REQUEST_GALLERY_PERMISSION) {
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            openGallery();
                        } else {
                            Toast.makeText(this, "Permission denied to access gallery", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(requestCode, resultCode, data);
                    if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            if (selectedImages.size() < 3) {
                                selectedImages.add(selectedImage);

                                // Set the image to the correct ImageView based on how many images are selected
                                if (selectedImages.size() == 1) {
                                    imageView.setImageURI(selectedImage);
                                } else if (selectedImages.size() == 2) {
                                    imageView2.setImageURI(selectedImage);
                                } else if (selectedImages.size() == 3) {
                                    imageView3.setImageURI(selectedImage);
                                }
                            } else {
                                // Show a toast if the limit is reached
                                Toast.makeText(this, "You can only add up to 3 images", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                private void showDeleteConfirmationDialog(int index) {
                    new AlertDialog.Builder(this)
                            .setTitle("Delete Image")
                            .setMessage("Do you want to delete this image?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                if (index < selectedImages.size()) {
                                    selectedImages.remove(index);
                                    updateImageViews();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

                private void updateImageViews() {
                    imageView.setImageURI(null);
                    imageView2.setImageURI(null);
                    imageView3.setImageURI(null);

                    if (selectedImages.size() > 0)
                        imageView.setImageURI(selectedImages.get(0));
                    if (selectedImages.size() > 1)
                        imageView2.setImageURI(selectedImages.get(1));
                    if (selectedImages.size() > 2)
                        imageView3.setImageURI(selectedImages.get(2));
                }


            }