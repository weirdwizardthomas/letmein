package com.via.letmein.ui.administration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.R;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<Member> data;

    public MemberAdapter(List<Member> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_member_item, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView portrait;
        TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            portrait = itemView.findViewById(R.id.memberCard_portrait);
            name = itemView.findViewById(R.id.memberCard_name);
        }
    }
}
