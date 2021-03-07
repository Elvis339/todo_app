package com.zone.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zone.todo.database.AppDatabase;
import com.zone.todo.database.ImageBitmapToString;
import com.zone.todo.entities.Task;
import com.zone.todo.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SingleTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extraTaskId";
    private static final int DEFAULT_TASK_ID = -1;

    private int mTaskId = DEFAULT_TASK_ID;
    private Task task;
    private TaskViewModel taskViewModel;

    Button deleteButton, updateButton;
    TextView datePicker;
    EditText taskName, taskDescription;
    ImageView attachment;

    String format = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
            task = AppDatabase.getAppDatabase(this).taskDao().findTaskById(mTaskId);
        }

        initView();
        setupViewModel();
    }


    private void setupViewModel() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
    }

    private void initView() {
        taskName = findViewById(R.id.taskName);
        taskDescription = findViewById(R.id.taskDescription);
        attachment = findViewById(R.id.attachment);
        datePicker = findViewById(R.id.datePicker);

        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        if (task != null) {
            taskName.setText(task.getName());
            taskDescription.setText(task.getDescription());
            attachment.setImageBitmap(ImageBitmapToString.StringToBitmap(task.getImageUri()));
            datePicker.setText(sdf.format(task.getDate()));
        }

        attachButtonListeners();
        attachTextWatchers();
    }

    private void attachButtonListeners() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                datePicker.setText(sdf.format(myCalendar.getTime()));
                task.setDate(myCalendar.getTime());

                taskViewModel.updateTask(task);
                showToast();
            }
        };

        datePicker.setOnClickListener(listener -> {
            DatePickerDialog dp = new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dp.show();
        });

        deleteButton.setOnClickListener(listener -> {
            taskViewModel.deleteTask(task);
            navigateBack();
        });

        updateButton.setOnClickListener(listener -> {
            taskViewModel.updateTask(task);
            showToast();
        });
    }

    private void attachTextWatchers() {
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && !task.getName().equals(s.toString())) {
                    task.setName(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        taskDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && !task.getDescription().equals(s.toString())) {
                    task.setDescription(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void navigateBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showToast() {
        Toast.makeText(this, "Successfully updated.", Toast.LENGTH_LONG).show();
    }
}