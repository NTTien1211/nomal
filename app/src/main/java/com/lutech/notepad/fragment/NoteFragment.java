package com.lutech.notepad.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lutech.notepad.R;
import com.lutech.notepad.acivity.MainActivity;
import com.lutech.notepad.adapter.NoteShowAdapter;
import com.lutech.notepad.acivity.NoteContentActivity;
import com.lutech.notepad.database.NoteDatabase;
import com.lutech.notepad.model.NotesModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NoteFragment extends Fragment implements NoteShowAdapter.OnItemLongClick {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclview;
//    Database database;
    List<NotesModel> mlist = new ArrayList<>();
    ;
    NoteShowAdapter adapter;
    private ActionMode myActMode;
    private static final int SORT_BY_TITLE_AZ = 0;

    private static final int SORT_BY_TITLE_ZA = 1;
    private static final int SORT_BY_DAY_NEWEST = 2;
    private static final int SORT_BY_DAY_OLDEST = 3;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();
    String categoryName;
    private clearmemu comment;

    private Context mContext;


//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//    }

    @Override
    public void onItemLongClick() {
        ((MainActivity)mContext).onItemLongClick();
    }

    public interface clearmemu {
        public void clearitemsmenu();
    }

    public void setComment(clearmemu comment) {
        this.comment = comment;
    }

    public clearmemu getComment() {
        return comment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mContext = requireContext();

        View view = inflater.inflate(R.layout.fragment__note, container, false);
//        database = new Database(getActivity(), "note.sqlite", null, 2);

        Bundle args = getArguments();
        categoryName = args != null ? args.getString("categoryName") : null;
        GetDataJob(categoryName);

        floatingActionButton = view.findViewById(R.id.btn_note_content);
        recyclview = view.findViewById(R.id.recyclview);
        adapter = new NoteShowAdapter(mlist);
        adapter.setOnitemLongClick(this);

        recyclview.setAdapter(adapter);
        recyclview.setLayoutManager(new LinearLayoutManager(getActivity()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NoteContentActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public static NoteFragment newInstance(String categoryName) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString("categoryName", categoryName);
        fragment.setArguments(args);
        return fragment;
    }


    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.layout_menu_handle, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Khi Contextual Action Mode kết thúc
        }
    };

    public void selectAllItems() {
        adapter.selectAll(); // Gọi phương thức selectAll của Adapter
    }

    public void deleteSelectedItems() {
        adapter.deleteSelectedItems(); // Gọi phương thức xóa các mục được chọn trong Adapter
    }
    public void clearSelection() {
        adapter.clearSelection(); // Gọi phương thức clearSelection của Adapter
    }

    public void selectAll() {
        for (int i = 0; i < mlist.size(); i++) {
            selectedPositions.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    private void GetDataJob(String categoryName) {
//        Cursor dataNote;
        if (categoryName == null || categoryName.equals("Notepad Free")) {
//            dataNote = database.GetData("SELECT * FROM NOTE WHERE Status = 'on'");
            hideSoftKeyboard();
            mlist = NoteDatabase.getInstance(getActivity()).noteDao().getListNoteOn();
        }else if (categoryName.equals( "Uncategorized")){
            hideSoftKeyboard();
            mlist = NoteDatabase.getInstance(getActivity()).noteDao().getNotesWithoutCategory();
        }
        else {
            hideSoftKeyboard();
            mlist = NoteDatabase.getInstance(getActivity()).noteDao().getNotesByCategory(categoryName);

        }
//

//

//        if (dataNote != null) {
//            mlist.clear();
//            while (dataNote.moveToNext()) {
//                int id = dataNote.getInt(0);
//                String name = dataNote.getString(1);
//                String note = dataNote.getString(2);
//                String day = dataNote.getString(3);
//                String time = dataNote.getString(4);
//                String status = dataNote.getString(5);
//                mlist.add(new NotesModel(id, name, note, day, time, status));
//            }
//
//            if (adapter != null) {
//                adapter.notifyDataSetChanged();
//            }
//        }

    }
    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData() {
        GetDataJob(categoryName);
        adapter.notifyDataSetChanged();
    }


    public void sortData(int sortBy) {
        switch (sortBy) {
            case 0:
                Collections.sort(mlist, new Comparator<NotesModel>() {
                    @Override
                    public int compare(NotesModel note1, NotesModel note2) {
                        return note1.getTitle().compareToIgnoreCase(note2.getTitle());
                    }
                });
                break;
            case 1:
                Collections.sort(mlist, new Comparator<NotesModel>() {
                    @Override
                    public int compare(NotesModel note1, NotesModel note2) {
                        return note2.getTitle().compareToIgnoreCase(note1.getTitle());
                    }
                });
                break;
            case 2:
                Collections.sort(mlist, new Comparator<NotesModel>() {
                    @Override
                    public int compare(NotesModel note1, NotesModel note2) {
                        return note2.getDay().compareTo(note1.getDay());
                    }
                });
                break;
            case 3:
                Collections.sort(mlist, new Comparator<NotesModel>() {
                    @Override
                    public int compare(NotesModel note1, NotesModel note2) {
                        return note1.getDay().compareTo(note2.getDay());
                    }
                });
                break;

        }


        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void filter(String keyword) {
        ArrayList<NotesModel> filteredList = new ArrayList<>();
        for (NotesModel note : mlist) {
            if (note.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                note.setKeyword(keyword);
                filteredList.add(note);
            }
        }
        adapter.setFilter(filteredList);
    }


}