package com.lutech.notepad.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "Note")
public class NotesModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Title;
    private String Content;
    private String day;
    private String time;
    private String status;
    private String keyword;

    public NotesModel() {
    }
//    public NotesModel(int id, String title, String content, String day, String time) {
//        this.id = id;
//        Title = title;
//        Content = content;
//        this.day = day;
//        this.time = time;
//    }
    public NotesModel(String title, String content, String day, String time, String status) {
        Title = title;
        Content = content;
        this.day = day;
        this.time = time;
        this.status = status;
    }

//    public NotesModel(int id, String title, String content, String day, String time, String status) {
//        this.id = id;
//        Title = title;
//        Content = content;
//        this.day = day;
//        this.time = time;
//        this.status = status;
//    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
