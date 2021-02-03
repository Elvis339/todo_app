package com.zone.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zone.todo.adapter.TaskAdapter;
import com.zone.todo.components.RecyclerViewSwipe;
import com.zone.todo.database.AppDatabase;
import com.zone.todo.entities.Task;
import com.zone.todo.viewmodel.TaskViewModel;


public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TaskAdapter mAdapter;
    private RecyclerView mRecyclerView;
    AppDatabase mDb;
    TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        // d
//        AppDatabase.deleteDatabase(getApplicationContext());
//        for (int i = 0; i < 101; i++) {
//            Task task = new Task("Task #Name" + i, "Desci: " + i, i % 2 == 0, i % 2 == 0, new Date());
//            AppDatabase.getAppDatabase(getApplicationContext()).taskDao().insertTask(task);
//            Log.d(TAG, task.toString());
//        }
////        new AsyncTask<Void, Void, Void>() {
////            @Override
////            protected Void doInBackground(Void... voids) {
////                for (int i = 0; i < 101; i++) {
////                    Task task = new Task("Task #Name" + i, "Desci: " + i, i % 2 == 0, i % 2 == 0, new Date());
////                    mDb.getAppDatabase(getApplicationContext()).taskDao().insertTask(task);
////                    Log.d(TAG, task.toString());
////                }
////                return null;
////            }
////        }.execute();
        init();
    }

    void init() {
        FloatingActionButton fab;
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.tasksRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TaskAdapter(this, this);
        setupViewModel();
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewSwipe(mAdapter, taskViewModel));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setupViewModel() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTasksObserver().observe(this, observer -> {
            Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
            mAdapter.setTasks(observer);
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, SingleTaskActivity.class);
        intent.putExtra(SingleTaskActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }
}