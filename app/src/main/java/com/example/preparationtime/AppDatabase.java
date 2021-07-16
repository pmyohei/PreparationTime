package com.example.preparationtime;
import androidx.room.Database;
import androidx.room.RoomDatabase;

/*
 * Database の定義
 */
@Database(entities = {TaskTable.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskTableDao taskTableDao();
}
