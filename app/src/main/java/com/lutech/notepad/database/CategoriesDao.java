package com.lutech.notepad.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lutech.notepad.model.CategoriesModel;
import com.lutech.notepad.model.NotesModel;

import java.util.List;

@Dao
public interface CategoriesDao {
    @Query("SELECT * FROM Categories")
    List<CategoriesModel> getListcategories();
    @Query("SELECT * FROM Categories WHERE Name != 'Uncategorized'")
    List<CategoriesModel> getListcategoriesOther();

    @Query("SELECT * FROM Categories where name = :categoryName")
    List<CategoriesModel> getListcategoriesName(String categoryName );

    @Query("UPDATE Categories SET name = :newName WHERE idCategory = :categoryId")
    void updateCategoryName(int categoryId, String newName);

    @Query("UPDATE Categories SET idNotes = :noteId WHERE idCategory = :categoryId")
    void updateCategoryNoteId(String categoryId, String noteId);
    @Query("DELETE FROM Categories WHERE idCategory = :categoryId")
    void deleteCategoryById(int categoryId);
    @Query("DELETE FROM Categories WHERE Name = 'Uncategorized'")
    void deleteUncategorizedCategory();
    @Query("SELECT COUNT(*) FROM Categories WHERE Name != 'Uncategorized'")
    int getNonUncategorizedCount();

    @Query("SELECT COUNT(*) FROM Categories")
    int getCategoryCount();
    @Insert
    void insertCategories(CategoriesModel categoriesModel);
}
