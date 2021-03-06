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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;

public class NoteFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static boolean mSubtitleVisible;
    private RecyclerView mNoteRecycleViewer;
    private NoteAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_notes, container, false);

        mNoteRecycleViewer = (RecyclerView) root.findViewById(R.id.notes_view);
        mNoteRecycleViewer.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            holder.bindNoteItem(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public void setScanItems(List<Note> notes) {
            mNotes = notes;
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Note mNote;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private RelativeLayout relativeLayout;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_note_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_note_date_text_view);
            mTimeTextView = (TextView) itemView.findViewById(R.id.list_note_time_text_view);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.note_item);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = NotesActivity.newIntent(getActivity(), mNote.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindNoteItem(Note note) {
            mNote = note;
            mTitleTextView.setText(mNote.getTitle());
            mDateTextView.setText(getDate(mNote));
            mTimeTextView.setText(getTime(mNote));
        }

        @Override
        public void onClick(View view) {
            Intent intent = NotesActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }
    }
}