package ru.eustrosoft.androidqr.service;

public class CommentsAlarmReceiver /*extends BroadcastReceiver*/ {
    public static long lastMessageDate;

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals("COMMENTS_NOTIFICATION")) {
//            Comment newComment = getNewMessageFromRequest(context);
//            if (newComment != null) {
//                Intent commentsIntent = new Intent(context, NavigationActivity.class);
//                commentsIntent.putExtra(FRAGMENT_ID, "comments");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                PendingIntent pendingIntent =
//                        PendingIntent.getActivity(context, 100, commentsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.comment_channel_id))
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentTitle(newComment.getUser() +
//                                " " + DateUtil.getFormattedDateAndTime(newComment.getDate()))
//                        .setContentText(newComment.getComment())
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setContentIntent(pendingIntent);
//
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
//                try {
//                    notificationManager.notify((int) UUID.randomUUID().getLeastSignificantBits(), builder.build());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    ToastHelper.toastCenter(context, ex.getMessage());
//                }
//            }
//        }
//    }
//
//    private Comment getNewMessageFromRequest(Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        String host = preferences.getString("host", "http://fudo.eustrosoft.org");
//        String port = preferences.getString("port", "20081");
//        String cookie = preferences.getString("cookie", "");
//        try {
//            HttpRequest request =
//                    HttpRequest.builder()
//                            .url(new URL(String.format("%s:%s/comments", host, port)))
//                            .headers(Map.of("Cookie", cookie))
//                            .method(HttpMethod.GET)
//                            .build();
//            HttpResponse response = request.request();
//            if (response.isOk()) {
//                JSONArray jsonArray = new JSONArray(response.getBody());
//                JSONObject object = null;
//                if (jsonArray.length() != 1) {
//                    for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++) {
//                        object = (JSONObject) jsonArray.get(i);
//                    }
//                } else {
//                    object = (JSONObject) jsonArray.get(0);
//                }
//                if (object != null) {
//                    Comment comment = new Comment(
//                            object.getString("user"),
//                            object.getString("comment"),
//                            new Date(object.getLong("commentDate"))
//                    );
//                    if (lastMessageDate == comment.getDate().getTime()) {
//                        return null;
//                    }
//                    lastMessageDate = comment.getDate().getTime();
//                    return comment;
//                }
//            }
//        } catch (NetworkErrorException | InterruptedException | JSONException | IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
