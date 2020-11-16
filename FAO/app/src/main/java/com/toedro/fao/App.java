package com.toedro.fao;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.toedro.fao.db.AppDatabase;

public class App extends Application {
    private static AppDatabase instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static AppDatabase getDBInstance() {
        if(instance == null) {
            instance = createDatabase(context);
        }
        return instance;
    }

    public static AppDatabase createDatabase(Context context) {
        return Room
                .databaseBuilder(context, AppDatabase.class, "FaoDB")
                .allowMainThreadQueries()
                //.fallbackToDestructiveMigration()
                .build();
    }
}
