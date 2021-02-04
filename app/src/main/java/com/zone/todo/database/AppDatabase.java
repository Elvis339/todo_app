package com.zone.todo.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.zone.todo.dao.TaskDao;
import com.zone.todo.entities.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters({DateConverter.class, ImageBitmapToString.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "todos";

    private static AppDatabase INSTANCE;
    public abstract TaskDao taskDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
        Log.d(LOG_TAG, DATABASE_NAME + " is cleared.");
    }
}
