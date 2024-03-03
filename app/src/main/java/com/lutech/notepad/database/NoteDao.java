package com.lutech.notepad.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lutech.notepad.model.CategoriesModel;
import com.lutech.notepad.model.NotesModel;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note")
    List<NotesModel> getAllNotes();
    @Query("SELECT * FROM Note WHERE Status = 'on'")
    List<NotesModel> getListNoteOn();
    @Query("SELECT * FROM Note WHERE Status = 'off'")
    List<NotesModel> getListNoteOff();
    @Query("UPDATE Note SET Status = 'off' WHERE Status = 'on' AND id = :noteId")
    void updateNoteStatusById(String noteId);
    @Query("UPDATE Note SET Status = 'on' WHERE Status = 'off' AND id = :noteId")
    void updateNoteStatusByIdON(String noteId);
    @Query("DELETE FROM Note WHERE id = :noteId")
    void deleteNoteById(String noteId);

    @Query("SELECT * FROM Note WHERE id IN (SELECT idNotes FROM Categories WHERE Name = :categoryName)")
    List<NotesModel> getNotesByCategory(String categoryName);
    @Query("SELECT * FROM Note LEFT JOIN Categories ON Note.id = Categories.idNotes WHERE Categories.idNotes IS NULL")
    List<NotesModel> getNotesWithoutCategory();



//    @Query("SELECT * FROM Note JOIN Categories ON Note.Id = Categories.Id_note WHERE Categories.Name = :categoryName")
//    List<NotesModel> getListNoteCategories(String categoryName);

    @Insert
    void insertNOTE(NotesModel note);


}
