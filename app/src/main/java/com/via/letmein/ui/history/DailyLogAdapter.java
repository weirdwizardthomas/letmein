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
import com.via.letmein.persistence.model.LoggedAction;
import com.via.letmein.persistence.model.DailyLog;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Adapter for {@see HistoryFragment#dayEntryRecyclerView} that displays all logs within a single day
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class DailyLogAdapter extends RecyclerView.Adapter<DailyLogAdapter.ViewHolder> {

    private List<DailyLog> data;
    private final Context context;

    public DailyLogAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public DailyLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_entry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyLogAdapter.ViewHolder holder, int position) {
        DailyLog dailyLog = data.get(position);
        formatDate(holder, dailyLog);
        setupHolderRecyclerView(holder, dailyLog);
    }

    private static void formatDate(@NonNull ViewHolder holder, DailyLog dayEntry) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        holder.date.setText(format.format(dayEntry.getTimestamp()));
    }

    private void setupHolderRecyclerView(@NonNull ViewHolder holder, DailyLog dayEntry) {
        holder.visitsList.setHasFixedSize(true);
        holder.visitsList.setLayoutManager(new LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false));
        holder.visitsList.setAdapter(new LogAdapter(dayEntry.getLoggedActions(), context));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<LoggedAction> content) {
        Map<Timestamp, List<LoggedAction>> groupLogsByDate = groupLogsByDate(content);
        updateData(groupLogsByDate);
        notifyDataSetChanged();
    }

    private void updateData(Map<Timestamp, List<LoggedAction>> days) {
        data = new ArrayList<>();
        for (HashMap.Entry<Timestamp, List<LoggedAction>> entry : days.entrySet())
            data.add(new DailyLog(entry.getKey(), entry.getValue()));
    }

    private static Map<Timestamp, List<LoggedAction>> groupLogsByDate(List<LoggedAction> content) {
        Map<Timestamp, List<LoggedAction>> days = new HashMap<>();

        for (LoggedAction loggedAction : content) {
            Timestamp timestamp = loggedAction.getTimestamp(LoggedAction.DATE_FORMAT_DATE_ONLY);

            if (days.containsKey(timestamp))
                Objects.requireNonNull(days.get(timestamp)).add(loggedAction);
            else {
                List<LoggedAction> loggedActionList = new ArrayList<>();
                loggedActionList.add(loggedAction);
                days.put(timestamp, loggedActionList);
            }
        }
        return days;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView date;
        final RecyclerView visitsList;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.timestampTextView);
            visitsList = itemView.findViewById(R.id.logsRecyclerView);
        }
    }
}
