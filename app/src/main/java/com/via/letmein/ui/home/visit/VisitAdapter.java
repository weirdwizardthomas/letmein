package com.via.letmein.ui.home.visit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.R;
import com.via.letmein.persistence.room.entity.Member;
import com.via.letmein.persistence.room.entity.Visit;

import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    private List<Visit> data;
    private final OnItemClickListener onItemClickListener;

    public VisitAdapter(List<Visit> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VisitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_visit_item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitAdapter.ViewHolder holder, int position) {
        Visit visit = data.get(position);
        Member member = visit.getMember();
        holder.image.setImageResource(member.getImageID());
        holder.name.setText(member.getName());
        holder.role.setText(member.getRole());

        //TODO Extracts hours and minutes from the timestamp
        holder.time.setText(visit.getTimestamp().toString().substring(11, 16));
    }

    public void setData(List<Visit> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView image;
        final TextView name;
        final TextView role;
        final TextView time;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.portrait);
            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            time = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick();
        }
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
}
