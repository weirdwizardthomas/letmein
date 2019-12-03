package com.via.letmein.ui.administration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.R;
import com.via.letmein.persistence.model.HouseholdMember;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<HouseholdMember> data;
    private OnItemClickListener onItemClickListener;

    MemberAdapter(OnItemClickListener onItemClickListener) {
        this.data = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.ViewHolder holder, int position) {
        HouseholdMember householdMember = data.get(position);
        //TODO load a profile image
        //holder.portrait.setImageResource(householdMember.getImageID());
        holder.name.setText(householdMember.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public HouseholdMember getMemberAt(int position) {
        return data.get(position);
    }

    void setData(List<HouseholdMember> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView portrait;
        TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            portrait = itemView.findViewById(R.id.portrait);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HouseholdMember item = data.get(getAdapterPosition());
            onItemClickListener.onItemClick(item);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(HouseholdMember item);
    }
}
