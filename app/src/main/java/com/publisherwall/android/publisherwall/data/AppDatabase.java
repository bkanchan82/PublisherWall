package com.publisherwall.android.publisherwall.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.publisherwall.android.publisherwall.NetworkDetailActivity;

@Database(entities = {NetworkEntry.class, EventEntry.class}
        , version = 2,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "publisher_wall_database";
    public static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getAppDatabaseInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();

            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract NetworkDao networkDao();

    public abstract EventDao eventDao();

    /*    public abstract TaskDao taskDao();
     */

}
