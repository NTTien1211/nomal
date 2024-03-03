package com.lutech.notepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lutech.notepad.R;

import java.util.List;

public class HelpNestedAdpter extends RecyclerView.Adapter<HelpNestedAdpter.NHelp_nested_ViewHolder> {
    private List<String> mList;
    public HelpNestedAdpter(List<String> mList){
        this.mList = mList;
    }
    @NonNull
    @Override
    public NHelp_nested_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_nested_item , parent , false);
        return new NHelp_nested_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NHelp_nested_ViewHolder holder, int position) {
        holder.mTv.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NHelp_nested_ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTv;
        public NHelp_nested_ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.nestedItemTv);
        }
    }
}
