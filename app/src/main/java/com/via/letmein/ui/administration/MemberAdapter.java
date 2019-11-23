package com.via.letmein.ui.administration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.R;
import com.via.letmein.persistence.entity.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<Member> data;
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
        Member member = data.get(position);

        holder.portrait.setImageResource(member.getImageID());
        holder.name.setText(member.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    Member getMemberAt(int position) {
        return data.get(position);
    }

    void setData(List<Member> data) {
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
            Member item = data.get(getAdapterPosition());
            onItemClickListener.onItemClick(item);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Member item);
    }
}
