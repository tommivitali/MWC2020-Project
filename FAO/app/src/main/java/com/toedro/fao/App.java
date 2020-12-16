package com.toedro.fao;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.toedro.fao.db.AppDatabase;

/**
 * This class is a singleton class that is useful to get some abstract methods to work with the Room
 * DB management.
 */
public class App extends Application {
    private static AppDatabase instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    /**
     * This function create a DB connection instance, if it does not exist already, and then returns
     * it.
     * @return a connection to the DB instance
     */
    public static AppDatabase getDBInstance() {
        if(instance == null) {
            instance = createDatabase(context);
        }
        return instance;
    }

    /**
     * This function create the 'FaoDB' database.
     * @param context current context
     * @return the Room DB connection
     */
    public static AppDatabase createDatabase(Context context) {
        return Room
                .databaseBuilder(context, AppDatabase.class, "FaoDB")
                .allowMainThreadQueries()
                // if you don't want to declare a migration but prefer to lose all your data passing
                // from a db version to another uncomment the following line
                //.fallbackToDestructiveMigration()
                .build();
    }
}
