package com.daliys.counter.ui.HistoryData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daliys.counter.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<HistoryItem> historyItems;


    public static class HistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewMainText;
        public TextView textViewDate;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMainText = itemView.findViewById(R.id.textViewMainText);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }

    public HistoryAdapter(ArrayList<HistoryItem> historyItems){
        this.historyItems = historyItems;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_added_item,parent,false);
        HistoryViewHolder hvh = new HistoryViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem currentItem = historyItems.get(position);

        holder.textViewDate.setText(currentItem.getFormattedDateText());
        holder.textViewMainText.setText(currentItem.getFormattedValueText());
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }


}
