package com.via.letmein.ui.history.visit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.persistence.entity.Member;
import com.via.letmein.R;
import com.via.letmein.persistence.entity.Visit;

import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    private List<Visit> data;

    public VisitAdapter(List<Visit> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VisitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_visit_item, parent, false);
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView role;
        TextView time;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.visitCard_image);
            name = itemView.findViewById(R.id.visitCard_name);
            role = itemView.findViewById(R.id.visitCard_role);
            time = itemView.findViewById(R.id.visitCard_time);
        }
    }
}
