package ru.eustrosoft.androidqr;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference.getKey().equals("signature")) {
                navigateTo(new LoginFragment(), true);
            }
            return super.onPreferenceTreeClick(preference);
        }

        public void navigateTo(Fragment fragment, boolean addToBackstack) {
            FragmentTransaction transaction =
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment);

            if (addToBackstack) {
                transaction.addToBackStack(null);
            }

            transaction.commit();
        }
    }
}