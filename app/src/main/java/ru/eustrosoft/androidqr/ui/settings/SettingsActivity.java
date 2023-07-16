package ru.eustrosoft.androidqr.ui.settings;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.service.CommentsAlarmReceiver;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        private static void createNotificationChannel(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.channel_name);
                String description = context.getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(context.getString(R.string.comment_channel_id), name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference.getKey().equals("comments_notifications")) { // TODO
                SwitchPreferenceCompat commentsNotification = (SwitchPreferenceCompat) preference;
                AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), CommentsAlarmReceiver.class);
                intent.setAction("COMMENTS_NOTIFICATION"); // TODO
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                if (commentsNotification.isChecked()) {
                    manager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5_000, 1_000, pendingIntent
                    );
                    createNotificationChannel(getContext());
                } else {
                    manager.cancel(pendingIntent);
                }
            }

            if (preference.getKey().equals("profiles")) {// TODO
                Intent intent = new Intent(getContext(), ProfilesActivity.class);
                startActivity(intent);
            }

            return super.onPreferenceTreeClick(preference);
        }


    }
}