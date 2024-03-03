package com.lutech.notepad.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.lutech.notepad.R;
import com.lutech.notepad.database.CategoriesDatabase;
import com.lutech.notepad.database.NoteDatabase;
import com.lutech.notepad.model.CategoriesModel;

import java.util.List;

public class EditCategoriesAdapter extends RecyclerView.Adapter<EditCategoriesAdapter.Edit_viewHolder> {
    List<CategoriesModel> mlist ;
    Context context ;
    Database database;
    CheckListenerChanged checkListenerChanged;
    private CategoriesDatabase mCategories;
    public interface CheckListenerChanged{
        void onCheckChange();
    }


    public void setCheckListenerChanged(CheckListenerChanged checkListenerChanged) {
        this.checkListenerChanged = checkListenerChanged;
    }

    public EditCategoriesAdapter(List<CategoriesModel> modelList , Context context,CategoriesDatabase categoriesDatabase ) {
        this.mlist = modelList;
        this.context = context;
        this.mCategories =categoriesDatabase;
        notifyDataSetChanged();
    }
    public EditCategoriesAdapter(List<CategoriesModel> modelList , Context context, Database database) {
        this.mlist = modelList;
        this.context = context;
        this.database =database;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Edit_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_catelogories_item, parent, false);
        return new EditCategoriesAdapter.Edit_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Edit_viewHolder holder, int position) {
        CategoriesModel item = mlist.get(position);
        holder.txt_Categories.setText(item.getName());
        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Category name");
                final EditText input = new EditText(context);
                input.setText(item.getName());
                builder.setView(input);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString();
//                        database.QueryData("UPDATE Categories SET Name='" + newName + "' WHERE Id=" + item.getIdCategory());
                        mCategories.categoriesDao().updateCategoryName(item.getIdCategory(), newName);
                        item.setName(newName);
                        Toast.makeText(context, "Category updated successfully", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        if(checkListenerChanged != null){
                            checkListenerChanged.onCheckChange();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        notifyDataSetChanged();
                        if(checkListenerChanged != null){
                            checkListenerChanged.onCheckChange();
                        }
                    }
                });

                builder.show();
            }
        });

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Category");
                builder.setMessage("Are you sure you want to delete this category?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int nonUncategorizedCount = mCategories.categoriesDao().getNonUncategorizedCount();
                        int totalCategoryCount = mCategories.categoriesDao().getCategoryCount();
                        mCategories.categoriesDao().deleteCategoryById(item.getIdCategory());
                        if (nonUncategorizedCount == 1 && totalCategoryCount > 1) {
                            mCategories.categoriesDao().deleteUncategorizedCategory();
                        }

//                        database.QueryData("DELETE FROM Categories WHERE Id=" + item.getIdCategory());
                        Toast.makeText(context, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                        mlist.remove(item);
                        notifyDataSetChanged();
                        if(checkListenerChanged != null){
                            checkListenerChanged.onCheckChange();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        notifyDataSetChanged();
                        if(checkListenerChanged != null){
                            checkListenerChanged.onCheckChange();
                        }
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Edit_viewHolder extends RecyclerView.ViewHolder{
        ImageButton btnedit , btndelete;
        TextView txt_Categories;
        public Edit_viewHolder(@NonNull View itemView) {
            super(itemView);

            btnedit = itemView.findViewById(R.id.btn_categories_fix);
            btndelete = itemView.findViewById(R.id.btn_categories_delete);
            txt_Categories = itemView.findViewById(R.id.txt_Categories);
        }
    }



}
