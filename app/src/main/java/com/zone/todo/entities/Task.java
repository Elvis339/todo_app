package com.zone.todo.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int taskId;

    @NonNull
    public String name, description;

    @Nullable
    public Date date;
    public boolean isCompleted, isStarred;

    public Task(@NonNull String name, @NonNull String description, @Nullable boolean isCompleted, @Nullable boolean isStarred, @Nullable Date date) {
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
        this.isStarred = isStarred;
        this.date = date;
    }

    public Task createDefaultTask(String name, String description) {
        this.name = name;
        this.description = description;
        this.date = new Date();
        this.isCompleted = false;
        return this;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Nullable
    public Date getDate() {
        return date;
    }

    public void setDate(@Nullable Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isCompleted=" + isCompleted +
                ", date=" + date +
                '}';
    }
}
