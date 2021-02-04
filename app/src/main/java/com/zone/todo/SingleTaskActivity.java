package com.zone.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

    Button deleteButton, saveButton, dateButton;

    String format = "MM/dd/yy HH:MM";
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
        EditText taskName, taskDescription;
        ImageView attachment;

        taskName = findViewById(R.id.taskName);
        taskDescription = findViewById(R.id.taskDescription);
        attachment = findViewById(R.id.attachment);

        deleteButton = findViewById(R.id.deleteButton);
        dateButton = findViewById(R.id.dateButton);

        if (task != null) {
            taskName.setText(task.getName());
            taskDescription.setText(task.getDescription());
            attachment.setImageBitmap(ImageBitmapToString.StringToBitmap(task.getImageUri()));
            dateButton.setText(sdf.format(task.getDate()));
        }

        attachButtonListeners();
    }

    private void attachButtonListeners() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dateButton.setText(sdf.format(myCalendar.getTime()));
                task.setDate(myCalendar.getTime());

                taskViewModel.updateTask(task);
                showToast();
            }
        };

        dateButton.setOnClickListener(listener -> {
            DatePickerDialog dp = new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dp.show();
        });

        deleteButton.setOnClickListener(listener -> {
            taskViewModel.deleteTask(task);
            navigateBack();
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