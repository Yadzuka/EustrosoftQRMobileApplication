package ru.eustrosoft.androidqr.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;

public class NotesFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static boolean mSubtitleVisible;
    private List<Note> selectedNotes;
    private RecyclerView mNoteRecycleViewer;
    private NoteAdapter mAdapter;
    private FloatingActionButton floatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        mNoteRecycleViewer = root.findViewById(R.id.notes_view);
        mNoteRecycleViewer.setLayoutManager(new LinearLayoutManager(getActivity()));

        floatingActionButton = root.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NoteActivity.class);
            startActivity(intent);
        });

        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        updateUI();

        mNoteRecycleViewer.getAdapter().notifyItemMoved(0, 5);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectedNotes = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        NoteLab noteLab = NoteLab.get(getActivity());
        List<Note> notes = noteLab.getNotes();
        Collections.reverse(notes);

        if (mAdapter == null) {
            mAdapter = new NoteAdapter(notes);
            mNoteRecycleViewer.setAdapter(mAdapter);
        } else {
            mAdapter.setScanItems(notes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private String getDate(Note mNote) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(mNote.getDate());
        return (date);
    }

    private String getTime(Note mNote) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = sdf.format(mNote.getTime());
        return (date);
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_note, parent, false);
            return new NoteHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            Note note = mNotes.get(position);
            holder.bindNoteItem(note, position);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public void setScanItems(List<Note> notes) {
            mNotes = notes;
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder {
        private Note mNote;
        private int mPosition;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private RelativeLayout relativeLayout;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.list_note_title_text_view);
            mDateTextView = itemView.findViewById(R.id.list_note_date_text_view);
            mTimeTextView = itemView.findViewById(R.id.list_note_time_text_view);
            relativeLayout = itemView.findViewById(R.id.note_item);


            relativeLayout.setOnClickListener(v -> {
                Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
                startActivity(intent);
            });

            relativeLayout.setOnLongClickListener(v -> {
                PopupMenu menu = new PopupMenu(getContext(), v);
                menu.getMenu().add("Open"); // TODO
                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("Delete")) {
                        NoteLab.get(getContext()).deleteNote(mNote);
                        updateUI();
                    }
                    if (item.getTitle().equals("Open")) {
                        Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
                        startActivity(intent);
                    }
                    return true;
                });
                menu.show();
                selectedNotes.add(mNote);
                return true;
            });
        }

        public void bindNoteItem(Note note, int position) {
            mNote = note;
            mPosition = position;
            mTitleTextView.setText(mNote.getTitle());
            mDateTextView.setText(getDate(mNote));
            mTimeTextView.setText(getTime(mNote));
        }
    }
}