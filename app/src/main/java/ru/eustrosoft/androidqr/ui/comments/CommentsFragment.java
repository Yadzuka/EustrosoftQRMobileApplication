package ru.eustrosoft.androidqr.ui.comments;

import android.accounts.NetworkErrorException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.service.CommentsAlarmReceiver;
import ru.eustrosoft.androidqr.util.inet.HttpMethod;
import ru.eustrosoft.androidqr.util.inet.HttpRequest;
import ru.eustrosoft.androidqr.util.inet.HttpResponse;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;


public class CommentsFragment extends Fragment {
    private RecyclerView mCommentsRecycleView;
    private CommentItemAdapter mCommentsAdapter;
    private EditText commentEditText;
    private Button commentSendButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments, container, false);
        mCommentsRecycleView = root.findViewById(R.id.comments_viewer);
        mCommentsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentEditText = root.findViewById(R.id.comment_edit_text);
        commentSendButton = root.findViewById(R.id.comment_send_button);
        commentSendButton.setOnClickListener(view -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String user = preferences.getString("username", "Undefined");
            String comment = commentEditText.getText().toString();
            if (!comment.trim().isEmpty()) {
                boolean success = sendComment(user, comment);
                if (success) {
                    commentEditText.setText("");
                    updateUI();
                }
            }
        });
        updateUI();
        mCommentsRecycleView.getAdapter().notifyItemMoved(0, 5);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_comments, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_comments:
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private boolean sendComment(String user, String comment) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String host = preferences.getString("host", "http://fudo.eustrosoft.org");
        String port = preferences.getString("port", "20081");
        String cookie = preferences.getString("cookie", "");
        JSONObject object = new JSONObject(
                Map.of(
                        "user", user,
                        "comment", comment
                )
        );
        try {
            HttpRequest request =
                    HttpRequest.builder()
                            .url(new URL(String.format("%s:%s/comments", host, port)))
                            .headers(Map.of("Cookie", cookie))
                            .method(HttpMethod.POST)
                            .body(object.toString())
                            .build();
            HttpResponse response = request.request();
            return response.isOk();
        } catch (Exception ex) {
            ToastHelper.toastCenter(getContext(), ex.getMessage());
            System.err.println(ex.getMessage());
        }
        return false;
    }

    private void updateUI() {
        List<Comment> comments = getComments();
        if (comments == null) {
            comments = new ArrayList<>();
        }

        if (mCommentsAdapter == null) {
            mCommentsAdapter = new CommentItemAdapter(comments);
            mCommentsRecycleView.setAdapter(mCommentsAdapter);
        } else {
            mCommentsAdapter.setScanItems(comments);
            mCommentsAdapter.notifyDataSetChanged();
        }
        mCommentsRecycleView.scrollToPosition(comments.size() - 1);
    }

    private List<Comment> getComments() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String host = preferences.getString("host", "http://fudo.eustrosoft.org");
        String port = preferences.getString("port", "20081");
        String cookie = preferences.getString("cookie", "");
        try {
            List<Comment> comments = new ArrayList<>();
            HttpRequest request =
                    HttpRequest.builder()
                            .url(new URL(String.format("%s:%s/comments", host, port)))
                            .headers(Map.of("Cookie", cookie))
                            .method(HttpMethod.GET)
                            .build();
            HttpResponse response = request.request();
            if (response.isOk()) {
                JSONArray jsonArray = new JSONArray(response.getBody());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    comments.add(new Comment(
                            object.getString("user"),
                            object.getString("comment"),
                            new Date(object.getLong("commentDate"))
                    ));
                }
                updateReceiveDateForNotifications(comments.get(comments.size() - 1));
                return comments;
            }
        } catch (NetworkErrorException | InterruptedException | JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateReceiveDateForNotifications(Comment comment) {
        CommentsAlarmReceiver.lastMessageDate = comment.getDate().getTime();
    }

    private String getDate(Comment mComment) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm:ss");
        return (sdf.format(mComment.getDate()));
    }

    private class CommentItemAdapter extends RecyclerView.Adapter<CommentHolder> {

        private List<Comment> mComments;

        public CommentItemAdapter(List<Comment> comments) {
            mComments = comments;
        }

        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_comment, parent, false);
            return new CommentHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.bindScanItem(comment);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void setScanItems(List<Comment> comments) {
            mComments = comments;
        }
    }

    private class CommentHolder extends RecyclerView.ViewHolder {
        private Comment mComment;
        private TextView mTitleTextView;
        private TextView mTextView;
        private TextView mDateTextView;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.comment_username);
            mTextView = (TextView) itemView.findViewById(R.id.comment_text);
            mDateTextView = (TextView) itemView.findViewById(R.id.comment_date);
        }

        public void bindScanItem(Comment comment) {
            mComment = comment;
            mTitleTextView.setText(mComment.getUser());
            mDateTextView.setText(getDate(mComment));
            mTextView.setText(mComment.getComment());
        }
    }
}
