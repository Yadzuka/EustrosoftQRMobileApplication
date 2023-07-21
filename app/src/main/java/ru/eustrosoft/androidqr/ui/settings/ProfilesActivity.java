package ru.eustrosoft.androidqr.ui.settings;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Profile;
import ru.eustrosoft.androidqr.model.ProfileDAO;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

import static ru.eustrosoft.androidqr.util.text.TextUtil.getChangedSymbolsText;

public class ProfilesActivity extends AppCompatActivity {
    private List<Profile> profiles;
    private RecyclerView profilesViewer;
    private ProfileAdapter profileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        profiles = new ArrayList<>();
        profilesViewer = findViewById(R.id.profiles_viewer);
        profilesViewer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
        profilesViewer.getAdapter().notifyItemMoved(0, 5);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_profile:
                showAddProfileDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profiles, menu);
        return true;
    }

    private List<Profile> getDBProfiles() {
        ProfileDAO profileDAO = ProfileDAO.get(getApplicationContext());
        return profileDAO.getProfiles();
    }

    private void updateUI() {
        List<Profile> profiles = getDBProfiles();
        Collections.reverse(profiles);

        if (profileAdapter == null) {
            profileAdapter = new ProfileAdapter(profiles);
            profilesViewer.setAdapter(profileAdapter);
        } else {
            profileAdapter.setProfiles(profiles);
            profileAdapter.notifyDataSetChanged();
        }
    }

    private void showAddProfileDialog() {
        AtomicReference<String> name = new AtomicReference<>("");
        AtomicReference<String> password = new AtomicReference<>("");

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilesActivity.this);
        builder.setTitle("Add new Profile");
        View alertDialogView = getLayoutInflater().inflate(R.layout.dialog_add_profile, null);
        EditText profileName = alertDialogView.findViewById(R.id.profile_name);
        EditText profilePassword = alertDialogView.findViewById(R.id.profile_password);
        profileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name.set(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        profilePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password.set(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        builder.setPositiveButton("Add", (dialog, which) -> {
            if (name != null && !name.get().isEmpty()) {
                Profile profile = new Profile();
                profile.setName(name.get());
                profile.setPassword(password.get());
                ProfileDAO profileDAO = ProfileDAO.get(getApplicationContext());
                Profile profileByName = profileDAO.getProfileByName(name.get());
                if (profileByName != null) {
                    ToastHelper.toastCenter(
                            getApplicationContext(),
                            getApplicationContext().getString(R.string.warning_profile_exists)
                    );
                    dialog.dismiss();
                    return;
                }
                profileDAO.addProfile(profile);
                updateUI();
                ToastHelper.toastCenter(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.message_profile_added)
                );
                dialog.dismiss();
            } else {
                ToastHelper.toastCenter(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.warning_profile_empty)
                );
            }
        });
        builder.setView(alertDialogView);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private class ProfileAdapter extends RecyclerView.Adapter<ProfileHolder> {
        private List<Profile> profiles;

        public ProfileAdapter(List<Profile> profiles) {
            this.profiles = profiles;
        }

        @NonNull
        @Override
        public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View v = layoutInflater.inflate(R.layout.list_profile, parent, false);
            return new ProfileHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
            Profile profile = profiles.get(position);
            holder.bindProfile(profile, position);
        }

        @Override
        public int getItemCount() {
            return profiles.size();
        }

        public void setProfiles(List<Profile> profile) {
            this.profiles = profile;
        }
    }

    private class ProfileHolder extends RecyclerView.ViewHolder {
        private Profile profile;
        private TextView name;
        private TextView password;
        private boolean passHidden = true;
        private int position;

        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.profile_name);
            password = itemView.findViewById(R.id.profile_password);
            this.itemView.setOnClickListener(this.getPasswordHideAction());
        }

        public void bindProfile(Profile profile, int position) {
            this.profile = profile;
            this.position = position;
            this.name.setText(this.profile.getName());
            this.password.setText(
                    getChangedSymbolsText(this.profile.getPassword(), "*", 12)
            );

        }

        private View.OnClickListener getPasswordHideAction() {
            return (v) -> {
                if (passHidden) {
                    this.password.setText(this.profile.getPassword());
                    this.passHidden = false;
                } else {
                    this.password.setText(getChangedSymbolsText(this.profile.getPassword(), "*", 12));
                    this.passHidden = true;
                }
            };
        }
    }
}