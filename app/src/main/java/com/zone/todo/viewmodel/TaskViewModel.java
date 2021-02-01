package com.zone.todo.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zone.todo.database.AppDatabase;
import com.zone.todo.entities.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private static final String TAG = TaskViewModel.class.getSimpleName();
    private LiveData<List<Task>> tasks;

    AppDatabase database;

    public TaskViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getAppDatabase(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasks = database.taskDao().getAllTasks();
    }

    public LiveData<List<Task>> loadAllTasks() {
        tasks = database.taskDao().getAllTasks();
        return tasks;
    }

    public LiveData<Task> getTaskById(int id) {
        return database.taskDao().getTaskById(id);
    }

    public LiveData<List<Task>> getTasksObserver() {
        return tasks;
    }
}
