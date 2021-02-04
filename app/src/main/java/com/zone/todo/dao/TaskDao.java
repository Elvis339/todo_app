package com.zone.todo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zone.todo.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task WHERE isCompleted = 0 ORDER BY date DESC")
    LiveData<List<Task>> getAllOpenTasks();

    @Query("SELECT * FROM task WHERE taskId = :id")
    LiveData<Task> getTaskById(int id);

    @Query("SELECT * FROM task WHERE taskId = :id")
    Task findTaskById(int id);

    @Query("SELECT * FROM TASK WHERE isStarred=1")
    List<Task> getAllStarredTasks();

    @Query("SELECT * FROM task WHERE date = DATE()")
    List<Task> getTodayTasks();

    @Query("SELECT COUNT(*) FROM task")
    int totalTasks();

    @Query("SELECT COUNT(*) FROM task WHERE date NOT NULL")
    int totalScheduledTasks();

    @Query("SELECT COUNT(*) FROM task WHERE isStarred=1")
    int totalStarredTasks();

    @Query("SELECT * FROM task WHERE name LIKE :slug")
    LiveData<List<Task>> searchTask(String slug);

    @Query("UPDATE task SET isCompleted = 1 WHERE taskId = :id")
    void completeTaskById(int id);

    @Insert
    void insertTask(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}
