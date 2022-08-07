package ru.eustrosoft.androidqr.ui.note;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;
import ru.eustrosoft.androidqr.util.text.KnuthMorrisSearch;
import ru.eustrosoft.androidqr.util.text.Searcher;
import ru.eustrosoft.androidqr.util.text.TextSearchParameters;

public class NotesFragment extends Fragment {
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
        updateUI();
        mNoteRecycleViewer.getAdapter().notifyItemMoved(0, 5);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_notes:
                showSearchWindow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSearchWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Search text");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        View alertDialogView = getLayoutInflater().inflate(R.layout.dialog_search, null);
        CheckBox searchInTitle = alertDialogView.findViewById(R.id.search_in_titles);
        CheckBox searchInText = alertDialogView.findViewById(R.id.search_in_text);
        CheckBox ignoreCase = alertDialogView.findViewById(R.id.ignore_case);
        EditText searchedText = alertDialogView.findViewById(R.id.searched_string);
        builder.setView(alertDialogView);
        builder.setPositiveButton("Search", (dialog, which) -> {
            searchTextInNotes(
                    new TextSearchParameters(
                            ignoreCase.isChecked(),
                            searchedText.getText().toString().toCharArray()
                    ),
                    searchInTitle.isChecked(),
                    searchInText.isChecked()
            );
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_notes, menu);
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private List<Note> getDBNotes() {
        NoteLab noteLab = NoteLab.get(getActivity());
        return noteLab.getNotes();
    }

    private void updateUI() {
        List<Note> notes = getDBNotes();
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

    private void searchTextInNotes(
            TextSearchParameters parameters,
            boolean searchInTitle,
            boolean searchInText
    ) {
        if (parameters == null) {
            parameters = new TextSearchParameters();
        }
        if (parameters.getSearchText() == null
                || new String(parameters.getSearchText()).trim().length() == 0) {
            updateUI();
            return;
        }
        final Searcher textSearch = new KnuthMorrisSearch(parameters);
        List<Note> notesToSearch = new ArrayList<>(NoteLab.get(getContext()).getNotes());
        try {
            selectedNotes = notesToSearch
                    .parallelStream()
                    .filter(note -> {
                        boolean passed = false;
                        if (searchInTitle) {
                            int res = textSearch.searchInText(
                                    note.getTitle().toCharArray()
                            );
                            if (res >= 0)
                                passed = true;
                        }
                        if (searchInText) {
                            int res = textSearch.searchInText(
                                    note.getText().toCharArray()
                            );
                            if (res >= 0)
                                passed = true;
                        }
                        return passed;
                    })
                    .collect(Collectors.toList());
            if (mAdapter == null) {
                mAdapter = new NoteAdapter(selectedNotes);
                mNoteRecycleViewer.setAdapter(mAdapter);
            } else {
                mAdapter.setScanItems(selectedNotes);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void revertSearchButton() {
        FragmentActivity act = getActivity();
        View searchButton = act.findViewById(R.id.action_search_notes);
        revertVisibility(searchButton);
    }

    private void revertVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
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