package com.via.letmein.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DayEntryAdapter extends RecyclerView.Adapter<DayEntryAdapter.ViewHolder> {

    private List<DayEntry> data;
    private Context context;

    public DayEntryAdapter(Context context, List<DayEntry> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public DayEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_history_entry_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayEntryAdapter.ViewHolder holder, int position) {
        DayEntry dayEntry = data.get(position);

        setupDate(holder, dayEntry);
        setupRecyclerView(holder, dayEntry);
    }

    private void setupDate(@NonNull ViewHolder holder, DayEntry dayEntry) {
        //TODO make universal, extract constant
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        holder.date.setText(format.format(dayEntry.getDate()));
    }

    private void setupRecyclerView(@NonNull ViewHolder holder, DayEntry dayEntry) {
        holder.visitsList.hasFixedSize();
        holder.visitsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.visitsList.setAdapter(new VisitAdapter(dayEntry.getVisits()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        RecyclerView visitsList;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dayEntries_date);
            visitsList = itemView.findViewById(R.id.dayEntries_recyclerView);
        }
    }
}
