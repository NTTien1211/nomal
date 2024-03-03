package com.lutech.notepad.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lutech.notepad.R;
import com.lutech.notepad.acivity.MainActivity;
import com.lutech.notepad.database.CategoriesDatabase;
import com.lutech.notepad.database.NoteDatabase;
import com.lutech.notepad.model.CategoriesModel;
import com.lutech.notepad.adapter.EditCategoriesAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class EditCategoriesFragment extends Fragment implements EditCategoriesAdapter.CheckListenerChanged{

    EditText name_categories;
    Button btnadd;
    RecyclerView reclyview_edit_categories;
    List<CategoriesModel> mlist;

    EditCategoriesAdapter adapter;
    String name;
    CategoriesDatabase categoriesDatabase;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_categories, container, false);
//        database = new Database(getActivity(), "note.sqlite", null, 2);
        name_categories = view.findViewById(R.id.name_categories);
        btnadd = view.findViewById(R.id.btn_addCategories);
        categoriesDatabase = CategoriesDatabase.getInstance(getContext());
        reclyview_edit_categories = view.findViewById(R.id.reclyview_edit_categories);
        mlist = new ArrayList<>();
        GetCatagori();
        adapter = new EditCategoriesAdapter(mlist, getContext(),categoriesDatabase);
        adapter.setCheckListenerChanged(this);
        reclyview_edit_categories.setAdapter(adapter);
        reclyview_edit_categories.setLayoutManager(new LinearLayoutManager(getActivity()));
//        database = new Database(getContext(), "note.sqlite", null, 2);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name = name_categories.getText().toString();
                if(name.equals("")|| name.isEmpty()) {
                    Toast.makeText(getContext(), "Categories is empty", Toast.LENGTH_SHORT).show();
                }else {
                    listener.onAddButtonClicked();
//                    GetCatagori();
                    LoadCategoriesTask loadCategoriesTask =  new LoadCategoriesTask(getContext(), name);
                    loadCategoriesTask.execute();
                    adapter.notifyDataSetChanged();
                    if (listener != null) {
                        listener.onAddButtonClicked();
                    }
                }


            }
        });
        return view;
    }
    public class LoadCategoriesTask extends AsyncTask<Void, Void, List<CategoriesModel>> {

        private WeakReference<Context> contextReference;
        private String name;

        LoadCategoriesTask(Context context, String name) {
            contextReference = new WeakReference<>(context);
            this.name = name;
        }

        @Override
        protected List<CategoriesModel> doInBackground(Void... voids) {
            Context context = contextReference.get();
            if (context == null) return null;

            CategoriesDatabase database = CategoriesDatabase.getInstance(context);

            List<CategoriesModel> existingCategories = database.categoriesDao().getListcategories();
            if (existingCategories == null || existingCategories.isEmpty()) {
                CategoriesModel uncategorizedCategory = new CategoriesModel("Uncategorized");
                database.categoriesDao().insertCategories(uncategorizedCategory);

                CategoriesModel newCategory = new CategoriesModel(name);
                database.categoriesDao().insertCategories(newCategory);
            } else {
                List<CategoriesModel> existingCategoryWithName = database.categoriesDao().getListcategoriesName(name);
                if (existingCategoryWithName == null || existingCategoryWithName.isEmpty()) {
                    CategoriesModel newCategory = new CategoriesModel(name);
                    database.categoriesDao().insertCategories(newCategory);
                }
            }

            return database.categoriesDao().getListcategories();
        }

        @Override
        protected void onPostExecute(List<CategoriesModel> categories) {
            if (contextReference.get() instanceof MainActivity) {
                MainActivity activity = (MainActivity) contextReference.get();
                // Cập nhật giao diện người dùng với danh sách danh mục đã nhận được
                // activity.updateUI(categories);

            }
        }
    }




    @Override
    public void onCheckChange() {
        GetCatagori();
        adapter.notifyDataSetChanged();
//        LoadCategoriesTask loadCategoriesTask =  new LoadCategoriesTask(getContext());
//        loadCategoriesTask.execute();

    }
    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            View currentFocus = getActivity().getCurrentFocus();
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public interface OnAddButtonClickListener {
        void onAddButtonClicked();
    }

    private OnAddButtonClickListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnAddButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnAddButtonClickListener");
        }
    }
    public void GetCatagori(){
//        Cursor dataCate = database.GetData("SELECT * FROM Categories");
//        mlist.clear();
//        if (dataCate!=null){
//            while (dataCate.moveToNext()){
//                int id = dataCate.getInt(0);
//                String name = dataCate.getString(1);
//                mlist.add(new CategoriesModel(id , name));
//            }
//
//        }
//        adapter.notifyDataSetChanged();
//    }
        hideSoftKeyboard();
        mlist = CategoriesDatabase.getInstance(getContext()).categoriesDao().getListcategoriesOther();
//        adapter.notifyDataSetChanged();

    }


}