package ru.eustrosoft.androidqr;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.eustrosoft.androidqr.model.ApplicationLogItem;

public class ApplicationLogActivity extends AppCompatActivity {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static boolean mSubtitleVisible;
    private RecyclerView mLogRecycleViewer;
    private LogAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_log);

        mLogRecycleViewer = (RecyclerView) findViewById(R.id.application_log_view);
        mLogRecycleViewer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        updateUI();
        mLogRecycleViewer.getAdapter().notifyItemMoved(0, 5);
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
        List<ApplicationLogItem> notes = new ArrayList<>();
        Resources resources = getResources();
        String[] titles = resources.getStringArray(R.array.application_log_title);
        String[] texts = resources.getStringArray(R.array.application_log_text);
        for (int i = 0; i < titles.length; i++) {
            notes.add(
                    new ApplicationLogItem(
                            titles[i],
                            texts[i]
                    )
            );
        }

        if (mAdapter == null) {
            mAdapter = new LogAdapter(notes);
            mLogRecycleViewer.setAdapter(mAdapter);
        } else {
            mAdapter.setScanItems(notes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class LogAdapter extends RecyclerView.Adapter<LogHolder> {
        private List<ApplicationLogItem> applicationLogs;

        public LogAdapter(List<ApplicationLogItem> notes) {
            applicationLogs = notes;
        }

        @NonNull
        @Override
        public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View v = layoutInflater.inflate(R.layout.activity_application_log_item, parent, false);
            return new LogHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull LogHolder holder, int position) {
            ApplicationLogItem note = applicationLogs.get(position);
            holder.bindScanItem(note);
        }

        @Override
        public int getItemCount() {
            return applicationLogs.size();
        }

        public void setScanItems(List<ApplicationLogItem> notes) {
            applicationLogs = notes;
        }
    }

    private class LogHolder extends RecyclerView.ViewHolder {
        private ApplicationLogItem applicationLogItem;
        private TextView mTitleTextView;
        private TextView mTextTextView;
        private RelativeLayout relativeLayout;

        public LogHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.application_log_title);
            mTextTextView = (TextView) itemView.findViewById(R.id.application_log_text);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.log_item);
        }

        public void bindScanItem(ApplicationLogItem log) {
            applicationLogItem = log;
            mTitleTextView.setText(applicationLogItem.getTitle());
            mTextTextView.setText(applicationLogItem.getText());
        }
    }
}