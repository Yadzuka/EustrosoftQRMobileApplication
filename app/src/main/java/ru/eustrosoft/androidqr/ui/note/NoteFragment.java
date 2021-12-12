package ru.eustrosoft.androidqr.ui.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;
import ru.eustrosoft.androidqr.model.ScanItemLab;

public class NoteFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static boolean mSubtitleVisible;
    private RecyclerView mNoteRecycleViewer;
    private NoteAdapter mAdapter;
    private TextView mTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        mNoteRecycleViewer = (RecyclerView) root.findViewById(R.id.notes_view);
        mNoteRecycleViewer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextView = (TextView) root.findViewById(R.id.no_notes);

        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        if (ScanItemLab.get(getActivity()).getScanItems().size() == 0)
            mTextView.setVisibility(View.VISIBLE);

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
            holder.bindScanItem(note);
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
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_scan_item_text_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_scan_item_date_text_view);
            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_scan_item_time_text_view);
        }

        public void bindScanItem(Note note) {
            mNote = note;
            mTitleTextView.setText(mNote.getText());
            mDateTextView.setText(getDate(mNote));
            mTimeTextView.setText(getTime(mNote));
        }
    }
}