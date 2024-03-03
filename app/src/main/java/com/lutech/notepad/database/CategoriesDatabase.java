package com.lutech.notepad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lutech.notepad.model.CategoriesModel;

@Database(entities = {CategoriesModel.class}, version = 3)
public abstract class CategoriesDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "NOTES";
    private static CategoriesDatabase instance;

    public static synchronized CategoriesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, CategoriesDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

            return instance;
        }
        return instance;
    }

    public abstract CategoriesDao categoriesDao();
}
