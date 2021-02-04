package com.zone.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.zone.todo.database.ImageBitmapToString;
import com.zone.todo.entities.Task;
import com.zone.todo.viewmodel.TaskViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = AddTaskActivity.class.getSimpleName();
    private EditText taskName, taskDescription;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mediaSwitch, cameraSwitch;
    private ImageView imageView;
    private Button saveButton;
    private String imageUri;
    TaskViewModel taskViewModel;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void init() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskName = findViewById(R.id.taskName);
        taskDescription = findViewById(R.id.taskDescription);
        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);
        mediaSwitch = findViewById(R.id.mediaSwitch);
        cameraSwitch = findViewById(R.id.cameraSwitch);

        mediaSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadImagesFromGallery();
                imageView.setVisibility(View.VISIBLE);
            }
            imageView.setVisibility(View.GONE);
        });

        cameraSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                askForCameraPermission();
                imageView.setVisibility(View.VISIBLE);
            }
            imageView.setVisibility(View.GONE);
        }));

        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setEnabled(s.length() > 2 && !s.toString().equals(" "));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveButton.setOnClickListener(listener -> {
            Task task = new Task(taskName.getText().toString());
            if (!taskDescription.getText().toString().equals(" ") && taskDescription.getText().length() > 3) {
                task.setDescription(taskDescription.getText().toString());
            }

            if (imageUri != null) {
                task.setImageUri(imageUri);
            }

            taskViewModel.createNewTask(task);
            Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void askForCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 101);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 102);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void loadImagesFromGallery() {
        if (ActivityCompat.checkSelfPermission(AddTaskActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddTaskActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        }

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        mediaSwitch.setChecked(false);
        cameraSwitch.setChecked(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            assert data != null;
            imageView.setVisibility(View.VISIBLE);
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageView.setVisibility(View.VISIBLE);
            ClipData clipData = data.getClipData();
            Bitmap bitmap = null;

            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        bitmap = BitmapFactory.decodeStream(is);
                        this.imageUri = ImageBitmapToString.BitmapToString(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(is);
                    this.imageUri = ImageBitmapToString.BitmapToString(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            imageView.setImageBitmap(bitmap);
        }
    }
}