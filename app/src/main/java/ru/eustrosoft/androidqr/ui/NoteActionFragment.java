package ru.eustrosoft.androidqr.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;

public class NoteActionFragment extends Fragment {
    private EditText title;
    private EditText text;
    private Button createButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note_creation, container, false);
        title = root.findViewById(R.id.note_title_edit_field);
        text = root.findViewById(R.id.note_text_edit_field);
        createButton = root.findViewById(R.id.note_save_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote(getContext(), title.getText().toString(), text.getText().toString());
            }
        });

        return root;
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
