package com.example.simpletodoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public interface onClickListener {
        void onItemClick(int position);
    }

    public interface onLongClickListener {
        void onItemLongClick(int position);
    }
    List<String> item;
    onLongClickListener LongClickListener;
    onClickListener ClickListener;

    public ItemAdapter(List<String> item, onLongClickListener LongClickListener, onClickListener ClickListener) {
        this.item = item;
        this.LongClickListener = LongClickListener;
        this.ClickListener = ClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflator to inflate a view
        View todo = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todo);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = item.get(position);
        holder.bind(s);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView etItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.etItem = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String s) {
            etItem.setText(s);
            etItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.onItemClick(getAdapterPosition());
                }
            });
            etItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    LongClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
