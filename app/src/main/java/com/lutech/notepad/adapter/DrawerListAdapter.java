package com.lutech.notepad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lutech.notepad.R;
import java.util.List;

public class DrawerListAdapter extends BaseAdapter {
    private Context context;
    private List<String> foldersList;

    public DrawerListAdapter(Context context, List<String> foldersList) {
        this.context = context;
        this.foldersList = foldersList;
    }

    @Override
    public int getCount() {
        return foldersList.size();
    }

    @Override
    public Object getItem(int position) {
        return foldersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }

        TextView txtTitle = convertView.findViewById(R.id.title);
        txtTitle.setText(foldersList.get(position));

        return convertView;
    }
}
