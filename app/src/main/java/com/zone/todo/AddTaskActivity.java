package com.zone.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = AddTaskActivity.class.getSimpleName();
    private EditText taskName, taskDescription;
    private CardView attachmentCardView;
    private Switch attachmentSwitch, locationSwitch;
    private ImageView imageView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        init();
    }

    private void init() {
        taskName = findViewById(R.id.taskName);
        taskDescription = findViewById(R.id.taskDescription);
        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);
        attachmentSwitch = findViewById(R.id.attachmentSwitch);
        locationSwitch = findViewById(R.id.locationSwitch);
        attachmentCardView = findViewById(R.id.attachmentCardView);

        attachmentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                askForCameraPermission();
                imageView.setVisibility(View.VISIBLE);
            }
            imageView.setVisibility(View.GONE);
        });

        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setEnabled(s.length() > 0 && !s.toString().equals(" "));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            assert data != null;
            imageView.setVisibility(View.VISIBLE);
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
        }
    }
}