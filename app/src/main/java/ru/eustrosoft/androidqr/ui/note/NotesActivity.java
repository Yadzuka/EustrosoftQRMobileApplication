package ru.eustrosoft.androidqr.ui.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;

import static ru.eustrosoft.androidqr.util.DateUtil.getFormattedDate;
import static ru.eustrosoft.androidqr.util.DateUtil.getFormattedTime;

public class NotesActivity extends AppCompatActivity {
    private static final String EXTRA_NOTE_ID = "ru.eustrosoft.noteid";

    private TextView titleTextView;
    private TextView textTextView;
    private TextView dateTextView;
    private TextView timeTextView;

    public static Intent newIntent(Context packageContext, UUID noteId){
        Intent intent = new Intent(packageContext, NotesActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initElements();

        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        if (noteId != null) {
            Note note = NoteLab.get(this).getNote(noteId);
            showNoteData(note);
        }
    }

    private void showNoteData(Note note) {
        titleTextView.setText(note.getTitle());
        textTextView.setText(note.getText());
        dateTextView.setText(getFormattedTime(note.getTime()));
        timeTextView.setText(getFormattedDate(note.getDate()));
    }

    private void initElements() {
        titleTextView = findViewById(R.id.notes_title_view);
        textTextView = findViewById(R.id.notes_text_view);
        dateTextView = findViewById(R.id.notes_date_view);
        timeTextView = findViewById(R.id.notes_time_view);
    }
}