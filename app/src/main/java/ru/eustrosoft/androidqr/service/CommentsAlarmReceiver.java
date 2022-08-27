package ru.eustrosoft.androidqr.service;

import android.accounts.NetworkErrorException;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.ui.NavigationActivity;
import ru.eustrosoft.androidqr.ui.comments.Comment;
import ru.eustrosoft.androidqr.util.DateUtil;
import ru.eustrosoft.androidqr.util.inet.HttpMethod;
import ru.eustrosoft.androidqr.util.inet.HttpRequest;
import ru.eustrosoft.androidqr.util.inet.HttpResponse;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

import static ru.eustrosoft.androidqr.ui.NavigationActivity.FRAGMENT_ID;

public class CommentsAlarmReceiver extends BroadcastReceiver {
    public static long lastMessageDate;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("COMMENTS_NOTIFICATION")) {
            Comment newComment = getNewMessageFromRequest(context);
            if (newComment != null) {
                Intent commentsIntent = new Intent(context, NavigationActivity.class);
                commentsIntent.putExtra(FRAGMENT_ID, "comments");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(context, 100, commentsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.comment_channel_id))
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(newComment.getUser() +
                                " " + DateUtil.getFormattedDateAndTime(newComment.getDate()))
                        .setContentText(newComment.getComment())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
                try {
                    notificationManager.notify((int) UUID.randomUUID().getLeastSignificantBits(), builder.build());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ToastHelper.toastCenter(context, ex.getMessage());
                }
            }
        }
    }

    private Comment getNewMessageFromRequest(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String host = preferences.getString("host", "http://fudo.eustrosoft.org");
        String port = preferences.getString("port", "20081");
        String cookie = preferences.getString("cookie", "");
        try {
            HttpRequest request =
                    HttpRequest.builder()
                            .url(new URL(String.format("%s:%s/comments", host, port)))
                            .headers(Map.of("Cookie", cookie))
                            .method(HttpMethod.GET)
                            .build();
            HttpResponse response = request.request();
            if (response.isOk()) {
                JSONArray jsonArray = new JSONArray(response.getBody());
                JSONObject object = null;
                if (jsonArray.length() != 1) {
                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++) {
                        object = (JSONObject) jsonArray.get(i);
                    }
                } else {
                    object = (JSONObject) jsonArray.get(0);
                }
                if (object != null) {
                    Comment comment = new Comment(
                            object.getString("user"),
                            object.getString("comment"),
                            new Date(object.getLong("commentDate"))
                    );
                    if (lastMessageDate == comment.getDate().getTime()) {
                        return null;
                    }
                    lastMessageDate = comment.getDate().getTime();
                    return comment;
                }
            }
        } catch (NetworkErrorException | InterruptedException | JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
