package com.zone.todo.fixtures;

import android.content.Context;

import com.zone.todo.database.AppDatabase;
import com.zone.todo.entities.Task;

public class TaskFixtures implements Fixtures {
    private AppDatabase db;

    public TaskFixtures(Context context) {
        db = AppDatabase.getAppDatabase(context);
    }

    @Override
    public void execute() {
        Task t1 = new Task("Watch Netflix");
        t1.setDescription("Find some enjoyable show");
        db.taskDao().insertTask(t1);

        Task t2 = new Task("Go to the store");
        t2.setDescription("Buy bread, milk, eggs");
        db.taskDao().insertTask(t2);

        Task t3 = new Task("Workout");
        t3.setDescription("Workout for 30 minutes");
        db.taskDao().insertTask(t3);

        Task t4 = new Task("Eat");
        t4.setDescription("Eat veggies");
        db.taskDao().insertTask(t4);
    }
}
