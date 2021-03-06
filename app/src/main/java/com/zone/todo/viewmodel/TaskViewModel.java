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
        tasks = database.taskDao().getAllOpenTasks();
    }

    public LiveData<List<Task>> loadAllOpenTasks() {
        tasks = AppDatabase.getAppDatabase(this.getApplication()).taskDao().getAllOpenTasks();
        return tasks;
    }

    public LiveData<Task> getTaskById(int id) {
        return database.taskDao().getTaskById(id);
    }

    public void deleteTask(Task task) {
        AppDatabase.getAppDatabase(this.getApplication()).taskDao().deleteTask(task);
    }

    public void completeTaskById(int id) {
        AppDatabase.getAppDatabase(this.getApplication()).taskDao().completeTaskById(id);
    }

    public void createNewTask(Task task) {
        AppDatabase.getAppDatabase(this.getApplication()).taskDao().insertTask(task);
    }

    public void updateTask(Task task) {
        AppDatabase.getAppDatabase(this.getApplication()).taskDao().updateTask(task);
    }

    public LiveData<List<Task>> searchTasks(String slug) {
        return AppDatabase.getAppDatabase(this.getApplication()).taskDao().searchTask("%" + slug + "%");
    }

    public LiveData<List<Task>> getTasksObserver() {
        return tasks;
    }
}
