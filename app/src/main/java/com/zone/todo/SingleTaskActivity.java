package com.zone.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SingleTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extraTaskId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);
    }
}