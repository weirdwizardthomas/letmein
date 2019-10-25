package com.via_android.letmein.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.via_android.letmein.Entities.DayEntry;
import com.via_android.letmein.Entities.Visit;
import com.via_android.letmein.R;

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
        View view = inflater.inflate(R.layout.activity_day_entries_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayEntryAdapter.ViewHolder holder, int position) {
        DayEntry dayEntry = data.get(position);

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        holder.date.setText(format.format(dayEntry.getDate()));

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
