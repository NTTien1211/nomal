package com.lutech.notepad.acivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Database;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lutech.notepad.R;
import com.lutech.notepad.database.CategoriesDatabase;
import com.lutech.notepad.database.NoteDatabase;
import com.lutech.notepad.model.CategoriesModel;
import com.lutech.notepad.model.NotesModel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import yuku.ambilwarna.AmbilWarnaDialog;


public class NoteContentActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SAVE_FILE = 1;
    private EditText title, main;
    private CharSequence originalText;
    //    Database database;
    String noteId;
    NotesModel mlist;
    LinearLayout layout_content_format;
    Toolbar actionBar;
    ImageButton btn_text_bold, btn_text_inher, btn_text_under, btn_text_bgcolor, btn_text_color, btn_text_size;
    boolean isBoldActivated = false;
    boolean isINActivated = false;
    boolean isUnActivated = false;
    private Stack<CharSequence> historyStack;

    private String textBeforeChange = "";
    List<String> nameCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_content);
        anhxa();

        historyStack = new Stack<>();
        setToolbar(actionBar, "Notepad Free");
//        database = new Database(this, "note.sqlite", null, 2);


        Intent intent = getIntent();
        String noteTitle = intent.getStringExtra("note_title");
        noteId = intent.getStringExtra("note_id");
        Toast.makeText(this, "" + noteId, Toast.LENGTH_SHORT).show();
        String noteContent = intent.getStringExtra("note_content");
        if (noteTitle == null && noteContent == null) {
            title.setText("");
            main.setText("");
        } else if (noteTitle.equals("") && noteContent.equals("")) {
            title.setText("");
            main.setText("");
        } else {
            title.setText(noteTitle);
            main.setText(noteContent);
        }

        main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(textBeforeChange)) {
                    historyStack.push(new SpannableStringBuilder(s.toString()));
                    textBeforeChange = s.toString();
                }
            }
        });
    }

    private void undo() {
        if (!historyStack.isEmpty()) {
            CharSequence previousText = historyStack.pop();
            if (previousText != null) {
                main.setText(previousText);
                main.setSelection(previousText.length());
            }
        }
    }

    private void anhxa() {
        title = findViewById(R.id.titile_content);
        main = findViewById(R.id.main_content);
        layout_content_format = findViewById(R.id.layout_content_format);
        actionBar = findViewById(R.id.content_toolbar);
        btn_text_bold = findViewById(R.id.btn_text_bold);
        btn_text_inher = findViewById(R.id.btn_text_inher);
        btn_text_under = findViewById(R.id.btn_text_under);
        btn_text_bgcolor = findViewById(R.id.btn_text_bgcolor);
        btn_text_color = findViewById(R.id.btn_text_color);
        btn_text_size = findViewById(R.id.btn_text_size);

    }

    private void setToolbar(Toolbar toolbar, String name) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_content_menuright, menu);
        changeMenuItemTextColor(menu, R.id.action_save, Color.WHITE);
        changeMenuItemTextColor(menu, R.id.action_undo, Color.WHITE);
        return true;
    }

    private void changeMenuItemTextColor(Menu menu, int menuItemId, int color) {
        MenuItem menuItem = menu.findItem(menuItemId);
        if (menuItem != null) {
            SpannableString spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_undo) {
            undo();
            return true;
        } else if (id == R.id.action_save) {
            String Datatitle = title.getText().toString();
            String Status = "on";
            String Datacontent = main.getText().toString();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
            int year = calendar.get(Calendar.YEAR);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            String Day = day + "/" + month + "/" + year;
            String Time = hour + ":" + minute;
            NotesModel notes = new NotesModel(Datatitle, Datacontent, Day, Time, "on");
//            NoteDatabase.getInstance(this).noteDao().insertNOTE(notes);
            if (noteId != null && !noteId.isEmpty()) {
//                database.QueryData("UPDATE NOTE SET Title='" + Datatitle + "', Content='" + Datacontent + "', DayNote='" + Day + "', TimeNote='" + Time + "', Status='" + Status + "' WHERE Id=" + noteId);
                NoteDatabase.getInstance(this).noteDao().insertNOTE(notes);

            } else {
                if (Datatitle.equals("") && Datacontent.equals("")) {
                    Datatitle = "Untitled";
                }
                NoteDatabase.getInstance(this).noteDao().insertNOTE(notes);
//                database.QueryData("INSERT INTO NOTE (Id, Title, Content, DayNote, TimeNote, Status ) VALUES (null, '" + Datatitle + "','" + Datacontent + "','"+Day+"','"+Time+"','"+Status+"');");
            }


            return true;
        } else if (id == R.id.action_share) {
            return true;
        } else if (id == R.id.action_delete) {
            String Status = "off";
//            database.QueryData("UPDATE NOTE SET Status='" + Status + "' WHERE Id=" + noteId);
            NoteDatabase.getInstance(this).noteDao().updateNoteStatusById(noteId);
            return true;
        } else if (id == R.id.action_export) {
            openFilePicker();
            return true;
        } else if (id == R.id.action_categorize) {
            showCategoryDialog();
            return true;
        } else if (id == R.id.action_formatting) {
            layout_content_format.setVisibility(View.VISIBLE);
            btn_text_bold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isBoldActivated) {
                        main.setTypeface(null, Typeface.NORMAL);
                    } else {
                        main.setTypeface(null, Typeface.BOLD);
                    }
                    isBoldActivated = !isBoldActivated;
                }
            });

            btn_text_inher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isINActivated) {
                        main.setTypeface(null, Typeface.NORMAL);
                    } else {
                        main.setTypeface(null, Typeface.ITALIC);
                    }
                    isINActivated = !isINActivated;
                }
            });
            btn_text_under.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUnActivated) {
                        main.setPaintFlags(main.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    } else {
                        main.setPaintFlags(main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }
                    isUnActivated = !isUnActivated; // Toggle the boolean value
                }
            });
            btn_text_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showColorPickerDialog();
                }
            });
            btn_text_bgcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showColorPickerDialogBG();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCategoryDialog() {
        List<String> categoryNames = getCategoryNames();

        CharSequence[] items = categoryNames.toArray(new CharSequence[categoryNames.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a category")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String categoryName = categoryNames.get(which);
                        addNoteToCategory(categoryName);
                    }
                });
        builder.show();
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showColorPickerDialog() {
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(this, Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                main.setTextColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                main.setTextColor(Color.BLACK);


            }
        });
        colorPickerDialog.show();
    }

    private void showColorPickerDialogBG() {
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(this, Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                SpannableString spannableString = new SpannableString(main.getText().toString());
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.RED);
                spannableString.setSpan(backgroundColorSpan, 0, main.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                main.setText(spannableString);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {


            }
        });
        colorPickerDialog.show();
    }


    private List<String> getCategoryNames() {
        List<CategoriesModel> categoryNames = new ArrayList<>();
        hideSoftKeyboard();
        categoryNames = CategoriesDatabase.getInstance(this).categoriesDao().getListcategoriesOther();
        for (CategoriesModel category : categoryNames) {
            int id = category.getIdCategory();
            String name = category.getName();
            nameCategories.add(name);
        }
        return nameCategories;
    }

    private void addNoteToCategory(String categoryName) {
        // Lấy categoryId dựa vào categoryName
        int categoryId = getCategoryId(categoryName);

        // Cập nhật noteId cho categoryId tương ứng
        CategoriesDatabase.getInstance(this).categoriesDao().updateCategoryNoteId(String.valueOf(categoryId), noteId);
    }

    private int getCategoryId(String categoryName) {
        List<CategoriesModel> categories = CategoriesDatabase.getInstance(this).categoriesDao().getListcategoriesName(categoryName);
        if (categories != null && !categories.isEmpty()) {
            return categories.get(0).getIdCategory();
        }
        return -1;
    }



    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain"); // Loại tệp bạn muốn tạo hoặc mở
        intent.putExtra(Intent.EXTRA_TITLE, "filename.txt"); // Tên mặc định của tệp

        startActivityForResult(intent, REQUEST_CODE_SAVE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SAVE_FILE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();

                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        String content = main.getText().toString();
                        outputStream.write(content.getBytes());
                        outputStream.close();
                        Toast.makeText(this, "File đã được lưu.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Đã xảy ra lỗi khi lưu file.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không có tệp nào được chọn hoặc xảy ra lỗi.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

//    private NotesModel GetDataJob(){
//        Cursor dataNote = database.GetData("SELECT * FROM NOTE WHERE Id = " + noteId);
//        if (dataNote.moveToNext()){
//            int id = dataNote.getInt(0);
//            String name = dataNote.getString(1);
//            String note = dataNote.getString(2);
//            String day = dataNote.getString(3);
//            String time = dataNote.getString(4);
//            String status = dataNote.getString(5);
//            mlist = new NotesModel(id, name, note,day, time,status);
//        }
//
//        return mlist;
//    }

}