package com.lutech.notepad.acivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.view.ActionMode;
import androidx.room.Database;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.lutech.notepad.R;
import com.lutech.notepad.adapter.NoteShowAdapter;
import com.lutech.notepad.database.CategoriesDatabase;
import com.lutech.notepad.fragment.NoteFragment;
import com.lutech.notepad.fragment.TrashFragment;
import com.lutech.notepad.fragment.EditCategoriesFragment;
import com.lutech.notepad.model.CategoriesModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EditCategoriesFragment.OnAddButtonClickListener, NoteShowAdapter.OnItemLongClick {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MenuItem deleteMenuItem;
    private Toolbar toolbar;
    private boolean isLongPressed;
    private Menu menu;
    private ActionMode actionMode;
    private boolean isDrawerOpen = false;

    List<CategoriesModel> mlist;
    Database database;
    private ActionMode myActMode;
    private boolean isActionModeActive = false;

    boolean ktra = false;
    Fragment selectedFragment;
    String fragmenttitle;
    NoteShowAdapter adapter;
    private boolean isActionModeShowing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        anhxa();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null && toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        }

        mlist = new ArrayList<>();
        adapter = new NoteShowAdapter();
        adapter.setOnitemLongClick(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        replaceFagment(new NoteFragment(), "Notepad Free");
        menu = navigationView.getMenu();
//        GetCatagori();
        new LoadCategoriesTask(this).execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (noteFragment != null) {
            noteFragment.onResume();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.layout_menu_toolbar, menu);


        MenuItem searchItem = menu.findItem(R.id.action_select);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.txt_search));

        MenuItem menuItem = menu.findItem(R.id.action_soft);
        if (menuItem != null) {
            SpannableString spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableString);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                NoteFragment fragmentNote = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
                if (fragmentNote != null) {
                    fragmentNote.filter(newText);
                }
                return true;
            }
        });

        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_soft) {
            showSortingDialog();
            return true;
        } else if (id == R.id.action_selectAll) {
            // Trong Activity của bạn
            NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            if (noteFragment != null) {
                noteFragment.selectAllItems();
            }

            toolbar.getMenu().clear();
            getMenuInflater().inflate(R.menu.layout_menu_handle, toolbar.getMenu());
            return true;
        } else if (id == R.id.action_deletehandler) {
            NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            if (noteFragment != null) {
                noteFragment.deleteSelectedItems();
            }
            toolbar.getMenu().clear();
            getMenuInflater().inflate(R.menu.layout_menu_toolbar, toolbar.getMenu());
        } else if (id == R.id.action_choiceall) {
            NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            if (noteFragment != null) {
                noteFragment.selectAllItems();
            }

            return true;
        }
        if (id == android.R.id.home) {
            if (isDrawerOpen) {
                // Đóng DrawerLayout nếu nó đang mở
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // Nếu DrawerLayout không mở, bạn sẽ hiển thị icon back
                isLongPressed = true;
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.iconsback30);
                toolbar.getMenu().clear();
                getMenuInflater().inflate(R.menu.layout_menu_handle, toolbar.getMenu());
                toolbar.invalidate();
            }
            // Cập nhật trạng thái của DrawerLayout
            isDrawerOpen = !isDrawerOpen;
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        selectedFragment = null;
        fragmenttitle = "";

        if (id == R.id.item_notes) {
            selectedFragment = NoteFragment.newInstance(null);
            fragmenttitle = "Notepad Free";
        } else if (id == R.id.item_editcategory) {
            selectedFragment = new EditCategoriesFragment();
            fragmenttitle = "Edit categories";
        } else if (id == R.id.item_trach) {
            selectedFragment = new TrashFragment();
            fragmenttitle = "Trash";
        } else if (id == R.id.item_backup) {
            Intent intent = new Intent(MainActivity.this, BackupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.item_setting) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.item_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            String categoryName = item.getTitle().toString();
            selectedFragment = NoteFragment.newInstance(categoryName);
            Toast.makeText(this, "" + categoryName, Toast.LENGTH_SHORT).show();
            fragmenttitle = categoryName;
        }
        replaceFagment(selectedFragment, fragmenttitle);

        drawerLayout.closeDrawer(GravityCompat.START);
        invalidateOptionsMenu();
        return true;
    }

    public void onItemClickView() {
        new LoadCategoriesTask(this).execute();
    }

    private void replaceFagment(Fragment fragment, String fragmenttitle) {
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putString("categoryName", fragmenttitle);
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_fragment, fragment);
            transaction.commit();
            getSupportActionBar().setTitle(fragmenttitle);

            invalidateOptionsMenu(); // Di chuyển invalidateOptionsMenu() vào đây
        } else {
            Log.e("User_Main_Activity", "Attempted to replace fragment with null");
        }
    }


    private void showSortingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by");
        String[] options = {"Titil: (A-Z)", "Title: (Z-A)", "Day (Mới nhất)", "Day (Cũ nhất)"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                NoteFragment fragmentNote = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
                if (fragmentNote != null) {
                    fragmentNote.sortData(i);
                }
            }
        });
        builder.create().show();
    }

    private void anhxa() {
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navi_view_left);
        toolbar = findViewById(R.id.toolbar_notes);
    }

    @Override
    public void onAddButtonClicked() {
        recreate();

    }

    public void GetCatagori() {
//        Cursor dataCate = database.GetData("SELECT * FROM Categories");
//        mlist.clear();
//        if (dataCate != null) {
//            while (dataCate.moveToNext()) {
//                int id = dataCate.getInt(0);
//                String name = dataCate.getString(1);
//                mlist.add(name);
//                navigationView.getMenu().add(R.id.menu_groups_add, id, id, name).setIcon(R.drawable.iconsfolder30);
//            }
//        }



    }

    public class LoadCategoriesTask extends AsyncTask<Void, Void, List<CategoriesModel>> {

        private WeakReference<MainActivity> activityReference;

        LoadCategoriesTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<CategoriesModel> doInBackground(Void... voids) {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return null;
            hideSoftKeyboard();
            mlist = CategoriesDatabase.getInstance(activity).categoriesDao().getListcategories();
            for (CategoriesModel category : mlist) {
                int id = category.getIdCategory();
                String name = category.getName();
                navigationView.getMenu().add(R.id.menu_groups_add, id, id, name).setIcon(R.drawable.iconsfolder30);
            }
            return mlist;
        }


        @Override
        protected void onPostExecute(List<CategoriesModel> categories) {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            // Gọi phương thức updateUI để cập nhật giao diện người dùng với dữ liệu từ cơ sở dữ liệu
//            activity.updateUI(categories);
        }

    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemLongClick() {
        isLongPressed = true;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.iconsback30);
        toolbar.getMenu().clear();
        getMenuInflater().inflate(R.menu.layout_menu_handle, toolbar.getMenu());
        toolbar.invalidate();

    }

//    private ActionMode.Callback myActModeCallback = new ActionMode.Callback() {
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            mode.getMenuInflater().inflate(R.menu.layout_menu_handle, menu);
//            mode.setTitle("");
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            int id = item.getItemId();
//                if (id == R.id.action_selectAll) {
//                    // Trong Activity của bạn
//                    NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
//                    if (noteFragment != null) {
//                        noteFragment.selectAllItems();
//                    }
//
//                    toolbar.getMenu().clear();
//                    getMenuInflater().inflate(R.menu.layout_menu_handle, toolbar.getMenu());
//                    return true;
//                } else if (id == R.id.action_deletehandler) {
//                    NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
//                    if (noteFragment != null) {
//                        noteFragment.deleteSelectedItems();
//                    }
//                    toolbar.getMenu().clear();
//                    getMenuInflater().inflate(R.menu.layout_menu_toolbar, toolbar.getMenu());
//                } else if (id == R.id.action_choiceall) {
//                    NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
//                    if (noteFragment != null) {
//                        noteFragment.selectAllItems();
//                    }
//
//                    return true;
//                }
//                return  false;
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            myActMode = null;
//            if (myActMode == null) {
//                toolbar.setVisibility(View.VISIBLE);
//            }
//            NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
//            if (noteFragment != null) {
//                noteFragment.clearSelection();
//            }
//        }
//
//    };

}