package ru.eustrosoft.androidqr.ui.note;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;

import static ru.eustrosoft.androidqr.util.ui.ToastHelper.toastCenter;

public class NoteCreationActivity extends AppCompatActivity {
    private static final String NOTE_CREATED = "New note was added";

    private EditText title;
    private EditText text;
    private Button createButton;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_creation);
        title = findViewById(R.id.note_title_edit_field);
        text = findViewById(R.id.note_text_edit_field);
        createButton = findViewById(R.id.note_save_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote(
                        getApplicationContext(),
                        title.getText().toString(), text.getText().toString()
                );
                toastCenter(getApplicationContext(), NOTE_CREATED);
            }
        });
    }

    private void createNote(Context context, String title, String text) {
        UUID noteId = UUID.randomUUID();
        Note note = new Note(noteId);
        note.setTitle(title);
        note.setText(text);
        NoteLab.get(context).addNote(note);
    }

    private void deleteNote(Context context, Note note) {
        NoteLab.get(context).deleteNote(note);
    }
}