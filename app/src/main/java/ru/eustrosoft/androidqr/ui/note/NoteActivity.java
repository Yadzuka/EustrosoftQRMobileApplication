package ru.eustrosoft.androidqr.ui.note;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteDAO;
import ru.eustrosoft.androidqr.ui.modals.ImageDisplayFragment;
import ru.eustrosoft.androidqr.ui.settings.SettingsActivity;
import ru.eustrosoft.androidqr.util.DateUtil;
import ru.eustrosoft.androidqr.util.file.FileSize;
import ru.eustrosoft.androidqr.util.file.FileUtil;
import ru.eustrosoft.androidqr.util.ui.ToastHelper;

import static ru.eustrosoft.androidqr.ui.qr.ScannerActivity.REQUEST_CODE;
import static ru.eustrosoft.androidqr.util.DateUtil.getFormattedDate;
import static ru.eustrosoft.androidqr.util.DateUtil.getFormattedTime;
import static ru.eustrosoft.androidqr.util.ui.ToastHelper.toastCenter;

public class NoteActivity extends AppCompatActivity {
    private static final String DIALOG_IMAGE = "DialogImage";

    private static final String EXTRA_NOTE_ID = "ru.eustrosoft.noteid";
    private static final String CAMERA_ERROR_TAG = "CAMERA";
    private static final String NOTE_UPDATED = "This note was updated";
    private static final String NOTE_CREATED = "New note was added";
    private static final int REQUEST_PHOTO = 3;

    private EditText titleTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private Note note;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    Uri image = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getApplicationContext().getContentResolver().query(
                            image,
                            filePathColumn,
                            null,
                            null,
                            null
                    );
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    File file = new File(picturePath);
                    File targetFile = createPhotoPathAndGetPath(note);

                    try {
                        FileUtil.copyFile(file, targetFile);
                        updatePhotoView();
                    } catch (Exception ex) {
                        ToastHelper.toastCenter(getApplicationContext(), "Can't load image from gallery.");
                    }
                }
            });
    private EditText textTextView;

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_add_image_from_gallery:
                handleAddPhotoFromGallery();
                return true;
            case R.id.action_save_note:
                handleSaveNote();
                return true;
            case R.id.action_delete_note:
                showAlertToDelete(note);
                return true;
            case R.id.action_send_note:
                updatePhotoDataAndView();
                sendNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO)
            updatePhotoView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initElements();

        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        if (noteId != null) {
            note = NoteDAO.get(this).getNote(noteId);
            showNoteData(note);
        } else {
            note = new Note();
            NoteDAO.get(getApplicationContext()).addNote(note);
        }
        updatePhotoView(note);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updatePhotoDataAndView();
    }

    private void updatePhotoDataAndView() {
        setDataToNote(note);
        NoteDAO.get(getApplicationContext()).updateNote(note);
        updatePhotoView(note);
    }

    private void showAlertToDelete(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this note?");
        builder.setMessage("");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            NoteDAO.get(getApplicationContext()).deleteNote(note);

            ToastHelper.toastCenter(
                    getApplicationContext(),
                    "Note was successfully deleted!"
            );
            finish();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void updatePhotoView(Note note) {
        File imageDirectory = NoteDAO.get(getApplicationContext()).getPhotosDirectory(note);
        if (imageDirectory == null) {
            return;
        }
        LinearLayout linearLayout = findViewById(R.id.layout_images_view);
        linearLayout.removeAllViews();
        File[] images = imageDirectory.listFiles();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.gravity = Gravity.CENTER;
        params.setMargins(12, 2, 12, 2);
        addNewImageButtonInHorizontalScroll(params, linearLayout);
        Arrays.stream(images)
                .forEach(img -> {
                    try {
                        final TextView textView = getImageTextViewWithPropsAndListeners(
                                params,
                                img
                        );

                        linearLayout.addView(textView);
                    } catch (Exception ex) {
                        Log.e("NOTE_IMAGE_TAG", "Image could not be set to textView");
                    }
                });
    }

    private TextView getImageTextViewWithPropsAndListeners(LinearLayout.LayoutParams params, File img)
            throws FileNotFoundException {
        TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setBackground(Drawable.createFromPath(img.toString()));
        textView.setText(FileSize.getPropertyFileSize(img));
        textView.setTextColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.BOTTOM);
        textView.setOnLongClickListener(view -> {
            PopupMenu menu = new PopupMenu(view.getContext(), view);
            menu.getMenu().add("Delete");
            menu.getMenu().add("View");
            menu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Delete")) {
                    boolean res = img.delete();
                    if (res) {
                        ToastHelper.toastCenter(view.getContext(), "Image was deleted!");
                    }
                    updatePhotoView();
                }
                if (item.getTitle().equals("View")) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    ImageDisplayFragment fragment = ImageDisplayFragment.newInstance(img);
                    fragment.show(fragmentManager, DIALOG_IMAGE);
                }
                return true;
            });
            menu.show();
            return true;
        });
        return textView;
    }

    private void addNewImageButtonInHorizontalScroll(
            LinearLayout.LayoutParams params,
            LinearLayout linearLayout
    ) {
        Drawable newImageIcon = getDrawable(R.drawable.ic_option_new_image);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setBackground(newImageIcon);
        textView.setOnClickListener(v -> handleAddPhoto());
        linearLayout.addView(textView);
    }

    private void setDataToNote(Note note) {
        note.setTitle(titleTextView.getText().toString());
        note.setText(textTextView.getText().toString());
    }

    private void showNoteData(Note note) {
        titleTextView.setText(note.getTitle());
        textTextView.setText(note.getText());
        dateTextView.setText(getFormattedDate(note.getDate()));
        timeTextView.setText(getFormattedTime(note.getTime()));
    }

    private void initElements() {
        titleTextView = findViewById(R.id.note_title_edit_field);
        textTextView = findViewById(R.id.note_text_edit_field);
        dateTextView = findViewById(R.id.note_date);
        timeTextView = findViewById(R.id.note_time);
    }

    private void handleSaveNote() {
        if (note.getId() == null) {
            String noteId = createNote(
                    getApplicationContext(),
                    titleTextView.getText().toString(),
                    textTextView.getText().toString()
            );
            Note note = new Note(UUID.fromString(noteId));
            note.setTitle(titleTextView.getText().toString());
            note.setText(textTextView.getText().toString());
            NoteDAO.get(getApplicationContext()).updateNote(note);
            toastCenter(getApplicationContext(), NOTE_CREATED);
        } else {
            NoteDAO.get(getApplicationContext()).updateNote(note);
            toastCenter(getApplicationContext(), NOTE_UPDATED);
        }
    }

    private void handleAddPhoto() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        try {
            Uri uri = FileProvider.getUriForFile(
                    getApplicationContext(),
                    "ru.eustrosoft.androidqr.ui.note.NoteCreationActivity.FileProvider",
                    createPhotoPathAndGetPath(note)
            );
            List<ResolveInfo> cameraActivities = getApplicationContext()
                    .getPackageManager()
                    .queryIntentActivities(
                            captureImage,
                            PackageManager.MATCH_DEFAULT_ONLY
                    );
            for (ResolveInfo activity : cameraActivities) {
                getApplicationContext()
                        .grantUriPermission(
                                activity.activityInfo.packageName,
                                uri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
            }
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(captureImage, REQUEST_PHOTO);
        } catch (Exception ex) {
            Log.e(CAMERA_ERROR_TAG, ex.toString());
        }
    }

    private void handleAddPhotoFromGallery() {
        Intent galleryPickIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        someActivityResultLauncher.launch(galleryPickIntent);
    }

    private File createPhotoPathAndGetPath(Note note) {
        return new File(
                NoteDAO.get(getApplicationContext()).getPhotosDirectory(note).getAbsolutePath(),
                String.format("%s.jpg", new SimpleDateFormat("dd:MM_HH:mm:ss").format(new Date()))
        );
    }

    private String createNote(Context context, String title, String text) {
        UUID noteId = UUID.randomUUID();
        Note note = new Note(noteId);
        note.setTitle(title);
        note.setText(text);
        NoteDAO.get(context).addNote(note);
        return noteId.toString();
    }

    private void updatePhotoView() {
        if (note == null) {
            return;
        }
        if (!NoteDAO.get(getApplicationContext()).getPhotosDirectory(note).exists())
            return;
        updatePhotoView(note);
    }

    private void sendNote() {
        if (note == null)
            return;

        File imageDirectory = NoteDAO.get(getApplicationContext()).getPhotosDirectory(note);
        if (imageDirectory == null) {
            return;
        }
        LinearLayout linearLayout = findViewById(R.id.layout_images_view);
        linearLayout.removeAllViews();
        File[] images = imageDirectory.listFiles();

        ShareCompat.IntentBuilder builder =
                ShareCompat.IntentBuilder
                        .from(this)
                        .setType("plain/text")
                        .setText(getNoteDataToSend())
                        .setSubject(getString(R.string.note_subject))
                        .setChooserTitle(R.string.note_chooser_title);

        for (File image : images) {
            Uri uriToFile = FileProvider.getUriForFile(
                    getApplicationContext(),
                    "ru.eustrosoft.androidqr.ui.note.NoteCreationActivity.FileProvider",
                    image);
            builder.addStream(uriToFile);
        }
        Intent intent = builder.createChooserIntent();
        startActivity(intent);
    }

    private String getNoteDataToSend() {
        String titleString = note.getTitle();
        String textString = note.getText();
        String dateString = DateUtil.getFormattedDate(note.getDate());
        String timeString = DateUtil.getFormattedTime(note.getTime());

        return getString(
                R.string.note_send_data,
                titleString,
                textString,
                dateString,
                timeString
        );
    }

    private void deleteNote(Context context, Note note) {
        NoteDAO.get(context).deleteNote(note);
    }
}