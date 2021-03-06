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
    public String name;

    @Nullable
    public String description;
    @Nullable
    public String imageUri;

    @Nullable
    public Date date;
    public boolean isCompleted, isStarred;

    public Task() {
        this.date = new Date();
    }

    public Task(@NonNull String name) {
        this.name = name;
        this.isStarred = false;
        this.isCompleted = false;
        this.date = new Date();
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

    @Nullable
    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(@Nullable String uri) {
        this.imageUri = uri;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isCompleted=" + isCompleted +
                ", date=" + date +
                ", imageUri=" + imageUri +
                '}';
    }
}
