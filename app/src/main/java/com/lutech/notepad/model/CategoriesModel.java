package com.lutech.notepad.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "Categories")
public class CategoriesModel {
    @PrimaryKey(autoGenerate = true)
    private int idCategory ;
    private String name ;
    private int idNotes;

    public CategoriesModel(String name) {
        this.name = name;
    }

//    public CategoriesModel(int idCategory, String name) {
//        this.idCategory = idCategory;
//        this.name = name;
//    }



    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdNotes() {
        return idNotes;
    }

    public void setIdNotes(int idNotes) {
        this.idNotes = idNotes;
    }
}
