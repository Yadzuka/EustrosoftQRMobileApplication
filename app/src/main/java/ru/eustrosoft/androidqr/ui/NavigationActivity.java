package ru.eustrosoft.androidqr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.ui.about.AboutActivity;
import ru.eustrosoft.androidqr.ui.about.ApplicationLogActivity;
import ru.eustrosoft.androidqr.ui.settings.SettingsActivity;

public class NavigationActivity extends AppCompatActivity {
    public static final String FRAGMENT_ID = "fragment";

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_notes, R.id.nav_database, R.id.nav_chat
        ).setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String fromNotification = extras.getString(FRAGMENT_ID);
            if (fromNotification.equals("comments")) {
                // TODO
//                CommentsFragment commentsFragment = new CommentsFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment, commentsFragment)
//                        .addToBackStack(null)
//                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_dev_log:
                Intent appLogActivity = new Intent(getApplicationContext(), ApplicationLogActivity.class);
                startActivity(appLogActivity);
                return true;
            case R.id.action_about:
                Intent aboutActivity = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}