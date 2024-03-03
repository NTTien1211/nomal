package com.lutech.notepad.acivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.lutech.notepad.R;
import com.lutech.notepad.adapter.HelpItemAdapter;
import com.lutech.notepad.model.Datamodel;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {
    private RecyclerView HelpMainRecyclerView;
    private Toolbar toolbar;
    private List<Datamodel> mList;
    private HelpItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        anhxa();
        setToolbar(toolbar, "Help");
        HelpMainRecyclerView.setHasFixedSize(true);
        HelpMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();

        List<String> nestedList1 = new ArrayList<>();
        nestedList1.add("A new note can be created by tapping the + (plus) icon located in the bottom right part of the notes list.A note can be deleted with the “Delete” button in the top right corner on the opened note screen. Deleted notes will be moved to the “Trash” folder by default (it can be changed in the app’s settings). To restore a note open the “Trash” folder from the main menu.Some operations, like deleting, can be performed on multiple notes. To select multiple notes hold a finger for a few seconds on a note list. This will turn on the selection mode, where it’s possible to select notes by tapping them on the list. Actions available for the selected notes are shown in the top part of the screen.Notes are saved automatically whenever it’s needed. There is no need to use the “Save” button. The app will detect when a note has been changed and needs to be saved, for example when switching apps or closing the note. Auto-saving can be disabled on the settings screen.After making an unwanted change while editing note, the “Undo” and “Redo” buttons can be used to remove that change. The changes can be undone only while the note is opened for edit. After closing a note the changes are saved permanently.Notes can be opened in read mode. In that mode a note can’t be changed and the keyboard won’t open. To change modes use the menu on the note screen.Notes can be grouped in categories. To create a new category open the app main menu and use the \"Edit categories\" function. This will open a new screen where categories can be added, edited, and removed. It is also possible to drag a category item and change order of categories.After adding a category it will be listed in the app's main menu, from where all notes in a given category can be listed");



        mList.add(new Datamodel(nestedList1 , "Expand all answers"));
        mList.add(new Datamodel(nestedList1 , "How can I use the app?"));
        mList.add(new Datamodel(nestedList1 , "My notes are lost. How can I recover them?"));
        mList.add(new Datamodel(nestedList1 , "How can I save notes to a new device?"));
        mList.add(new Datamodel(nestedList1 , "How can I load to a backup copy file?"));
        mList.add(new Datamodel(nestedList1 , "How can I load to a backup copy file?"));

        adapter = new HelpItemAdapter(mList);
        HelpMainRecyclerView.setAdapter(adapter);
    }

    private void anhxa() {
        HelpMainRecyclerView = findViewById(R.id.Help_main_recycler);
        toolbar = findViewById(R.id.help_toolbar);

    }
    private void setToolbar(androidx.appcompat.widget.Toolbar toolbar, String name){
        setSupportActionBar(toolbar);
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
        toolbar.setNavigationIcon(R.drawable.iconsback30);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}