package com.lutech.notepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lutech.notepad.R;
import com.lutech.notepad.model.Datamodel;

import java.util.ArrayList;
import java.util.List;

public class HelpItemAdapter extends RecyclerView.Adapter<HelpItemAdapter.Help_item_viewholder> {
    private List<Datamodel> mlist;
    private List<String> list = new ArrayList<>();
    public HelpItemAdapter(List<Datamodel> mList){
        this.mlist  = mList;
    }
    @NonNull
    @Override
    public Help_item_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_each_item, parent, false);
       return new Help_item_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Help_item_viewholder holder, int position) {
        Datamodel   datamodel = mlist.get(position);
        holder.mtext.setText(datamodel.getItemText());
        boolean isExpandable = datamodel.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable? View.VISIBLE : View.GONE);
        if  (isExpandable) {
            holder.mImg.setImageResource(R.drawable.iconsline30);
        }else {
            holder.mImg.setImageResource(R.drawable.iconsplusmath30);
        }

        HelpNestedAdpter adapter = new HelpNestedAdpter(list);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datamodel.setExpandable(!datamodel.isExpandable());
                list= datamodel.getNestedList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Help_item_viewholder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
         private RecyclerView      nestedRecyclerView;
        private TextView mtext;
        private ImageView mImg;
        public Help_item_viewholder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
            mtext = itemView.findViewById(R.id.itemTv);
            mImg = itemView.findViewById(R.id.arro_imageview);
        }
    }
}
