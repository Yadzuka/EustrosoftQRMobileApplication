package ru.eustrosoft.androidqr.ui.note;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;
import java.util.UUID;

import ru.eustrosoft.androidqr.R;
import ru.eustrosoft.androidqr.model.Note;
import ru.eustrosoft.androidqr.model.NoteLab;
import ru.eustrosoft.androidqr.util.PictureUtils;

import static ru.eustrosoft.androidqr.ScannerActivity.REQUEST_CODE;
import static ru.eustrosoft.androidqr.util.ui.ToastHelper.toastCenter;

public class NoteCreationActivity extends AppCompatActivity {
    private static final String NOTE_CREATED = "New note was added";

    private static final int REQUEST_PHOTO = 3;
    private final static String CAMERA_ERROR_TAG = "CAMERA";

    private EditText title;
    private EditText text;
    private Button createButton;
    private Button createPhotoButton;

    private ImageView noteImageView;

    private File mPhotoFile;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_creation);
        title = findViewById(R.id.note_title_edit_field);
        text = findViewById(R.id.note_text_edit_field);
        createButton = findViewById(R.id.note_save_button);

        String noteId = createNote(
                getApplicationContext(),
                title.getText().toString(), text.getText().toString()
        );
        mPhotoFile = NoteLab.get(getApplicationContext())
                .getPhotoFile(NoteLab.get(getApplicationContext())
                        .getNote(UUID.fromString(noteId)));

        noteImageView = findViewById(R.id.note_image_view);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(UUID.fromString(noteId));
                note.setTitle(title.getText().toString());
                note.setText(text.getText().toString());
                NoteLab.get(getApplicationContext()).updateScanItem(note);
                toastCenter(getApplicationContext(), NOTE_CREATED);
            }
        });

        createPhotoButton = findViewById(R.id.note_photo_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        createPhotoButton.setOnClickListener(v -> {
            try {
                Uri uri = FileProvider.getUriForFile(
                        getApplicationContext(),
                        "ru.eustrosoft.androidqr.ui.note.NoteCreationActivity.FileProvider",
                        mPhotoFile);
                List<ResolveInfo> cameraActivities = getApplicationContext()
                        .getPackageManager()
                        .queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
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

        });
        updatePhotoView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO)
            updatePhotoView();
    }

    private String createNote(Context context, String title, String text) {
        UUID noteId = UUID.randomUUID();
        Note note = new Note(noteId);
        note.setTitle(title);
        note.setText(text);
        NoteLab.get(context).addNote(note);
        return noteId.toString();
    }

    private void updatePhotoView() {
        if (noteImageView == null || !mPhotoFile.exists())
            noteImageView.setImageDrawable(null);
        else {
            Bitmap image = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), this);
            noteImageView.setImageBitmap(image);
        }
    }

    private void deleteNote(Context context, Note note) {
        NoteLab.get(context).deleteNote(note);
    }
}