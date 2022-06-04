package ru.eustrosoft.androidqr.ui.history;

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
import ru.eustrosoft.androidqr.model.ScanItem;
import ru.eustrosoft.androidqr.model.ScanItemLab;


public class HistoryFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final int REQUEST_CODE_CHANGE = 0;
    private static boolean mSubtitleVisible;
    private RecyclerView mScanItemRecyclerView;
    private ScanItemAdapter mAdapter;
    private TextView mTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        mScanItemRecyclerView = (RecyclerView) root.findViewById(R.id.scan_history_view);
        mScanItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextView = (TextView) root.findViewById(R.id.no_history);

        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        if (ScanItemLab.get(getActivity()).getScanItems().size() == 0)
            mTextView.setVisibility(View.VISIBLE);

        updateUI();

        mScanItemRecyclerView.getAdapter().notifyItemMoved(0, 5);

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
        ScanItemLab scanItemLab = ScanItemLab.get(getActivity());
        List<ScanItem> scanItems = scanItemLab.getScanItems();
        Collections.reverse(scanItems);

        if (mAdapter == null) {
            mAdapter = new ScanItemAdapter(scanItems);
            mScanItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setScanItems(scanItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    private String getDate(ScanItem mScanItem) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(mScanItem.getDate());
        return (date);
    }

    private String getTime(ScanItem mScanItem) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = sdf.format(mScanItem.getTime());
        return (date);
    }

    private class ScanItemAdapter extends RecyclerView.Adapter<ScanItemHolder> {

        private List<ScanItem> mScanItems;

        public ScanItemAdapter(List<ScanItem> scanItems) {
            mScanItems = scanItems;
        }

        @NonNull
        @Override
        public ScanItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_scan_item, parent, false);
            return new ScanItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ScanItemHolder holder, int position) {
            ScanItem ScanItem = mScanItems.get(position);
            holder.bindScanItem(ScanItem);
        }

        @Override
        public int getItemCount() {
            return mScanItems.size();
        }

        public void setScanItems(List<ScanItem> ScanItems) {
            mScanItems = ScanItems;
        }
    }

    private class ScanItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ScanItem mScanItem;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private RelativeLayout mRelativeLayout;

        public ScanItemHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_scan_item_text_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_scan_item_date_text_view);
            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_scan_item_time_text_view);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.scan_item);

            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = HistoryItemActivity.newIntent(getActivity(), mScanItem.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindScanItem(ScanItem ScanItem) {
            mScanItem = ScanItem;
            mTitleTextView.setText(mScanItem.getText());
            mDateTextView.setText(getDate(mScanItem));
            mTimeTextView.setText(getTime(mScanItem));
        }

        @Override
        public void onClick(View v) {
            Intent intent = HistoryItemActivity.newIntent(getActivity(), mScanItem.getId());
            startActivity(intent);
        }
    }
}
