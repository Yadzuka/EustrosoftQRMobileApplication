package ru.eustrosoft.androidqr.ui.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;
import ru.eustrosoft.androidqr.util.PictureUtils;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

import static ru.eustrosoft.androidqr.util.DateUtil.getFormattedDate;
import static ru.eustrosoft.androidqr.util.DateUtil.getFormattedTime;

public class NotesActivity extends AppCompatActivity {
    private static final String EXTRA_NOTE_ID = "ru.eustrosoft.noteid";

    private TextView titleTextView;
    private TextView textTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private ImageView noteImageView;
    private Button deleteNoteButton;
    private Button saveNoteButton;

    private Note note;

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NotesActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initElements();

        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        if (noteId != null) {
            note = NoteLab.get(this).getNote(noteId);
            showNoteData(note);
            updatePhotoView(note);
            deleteNoteButton.setOnClickListener(v -> {
                showAlertToDelete(note);
            });
            saveNoteButton.setOnClickListener(v -> {
                setDataToNote(note);
                NoteLab.get(getApplicationContext()).updateScanItem(note);
                ToastHelper.toastCenter(
                        getApplicationContext(),
                        "Note was successfully saved!"
                );
            });
        }
    }

    private void showAlertToDelete(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this note?");
        builder.setMessage("");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            NoteLab.get(getApplicationContext()).deleteNote(note);
            ToastHelper.toastCenter(
                    getApplicationContext(),
                    "Note was successfully deleted!"
            );
            finish();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
        });
        builder.show();
    }

    private void updatePhotoView(Note note) {
        File file = NoteLab.get(getApplicationContext()).getPhotoFile(note);
        if (noteImageView == null || !file.exists())
            noteImageView.setImageDrawable(null);
        else {
            Bitmap image = PictureUtils.getScaledBitmap(
                    file.getPath(), this);
            noteImageView.setImageBitmap(image);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        setDataToNote(note);
        NoteLab.get(getApplicationContext()).updateScanItem(note);
    }

    private void setDataToNote(Note note) {
        note.setTitle(titleTextView.getText().toString());
        note.setText(textTextView.getText().toString());
    }

    private void showNoteData(Note note) {
        titleTextView.setText(note.getTitle());
        textTextView.setText(note.getText());
        dateTextView.setText(getFormattedDate(note.getDate()));
        timeTextView.setText(getFormattedTime(note.getTime()));
    }

    private void initElements() {
        titleTextView = findViewById(R.id.notes_title_view);
        textTextView = findViewById(R.id.notes_text_view);
        dateTextView = findViewById(R.id.notes_date_view);
        timeTextView = findViewById(R.id.notes_time_view);
        deleteNoteButton = findViewById(R.id.delete_note_button);
        saveNoteButton = findViewById(R.id.save_note_button);
        noteImageView = findViewById(R.id.notes_image_view);
    }
}