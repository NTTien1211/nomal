package com.lutech.notepad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lutech.notepad.database.NoteDao;
import com.lutech.notepad.model.CategoriesModel;
import com.lutech.notepad.model.NotesModel;

@Database(entities = {NotesModel.class , CategoriesModel.class}, version = 4)
public abstract class NoteDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "NOTES";
    private static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, NoteDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

            return instance;
        }
        return instance;
    }

    public abstract NoteDao noteDao();
}
