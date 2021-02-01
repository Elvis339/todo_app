package com.zone.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.zone.todo.adapter.TaskAdapter;
import com.zone.todo.dao.TaskDao;
import com.zone.todo.database.AppDatabase;
import com.zone.todo.entities.Task;
import com.zone.todo.viewmodel.TaskViewModel;

import java.lang.ref.WeakReference;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TaskAdapter mAdapter;
    AppDatabase mDb;
    TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // d
        AppDatabase.deleteDatabase(getApplicationContext());
        for (int i = 0; i < 101; i++) {
            Task task = new Task("Task #Name" + i, "Desci: " + i, i % 2 == 0, i % 2 == 0, new Date());
            AppDatabase.getAppDatabase(getApplicationContext()).taskDao().insertTask(task);
            Log.d(TAG, task.toString());
        }
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                for (int i = 0; i < 101; i++) {
//                    Task task = new Task("Task #Name" + i, "Desci: " + i, i % 2 == 0, i % 2 == 0, new Date());
//                    mDb.getAppDatabase(getApplicationContext()).taskDao().insertTask(task);
//                    Log.d(TAG, task.toString());
//                }
//                return null;
//            }
//        }.execute();
        init();
    }

    void setupCounters() {
        TextView todayCounter, scheduledCounter, allCounter, starredCounter;

        TaskDao db = AppDatabase.getAppDatabase(getApplicationContext()).taskDao();

        int allCount = db.totalTasks();
        allCounter = findViewById(R.id.allCounter);
        allCounter.setText(String.valueOf(allCount));

        int starredCount = db.totalStarredTasks();
        starredCounter = findViewById(R.id.starredCounter);
        starredCounter.setText(String.valueOf(starredCount));

        int scheduledCount = db.totalScheduledTasks();
        scheduledCounter = findViewById(R.id.scheduledCounter);
        scheduledCounter.setText(String.valueOf(scheduledCount));
    }

    void init() {
        setupCounters();
        RecyclerView mRecyclerView = findViewById(R.id.tasksRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TaskAdapter(this, this);
        setupViewModel();
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
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
        // Launch AddTaskActivity adding the itemId as an extra in the intent
        // COMPLETED (2) Launch AddTaskActivity with itemId as extra for the key AddTaskActivity.EXTRA_TASK_ID
//        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
//        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, itemId);
//        startActivity(intent);
        Log.d(TAG, "Task ID -> " + itemId);
    }

    public void setResult(Task task, int i) {
        Log.d(TAG, task.toString());
    }
}