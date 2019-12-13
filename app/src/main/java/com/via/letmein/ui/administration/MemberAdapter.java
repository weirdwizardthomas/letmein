package com.via.letmein.ui.administration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.HouseholdMember;

import java.util.ArrayList;
import java.util.List;

import static com.via.letmein.persistence.api.Api.ADDRESS_PORT_DELIMITER;
import static com.via.letmein.persistence.api.Api.HTTP;
import static com.via.letmein.persistence.api.Api.PARAMETER_DELIMITER;
import static com.via.letmein.persistence.api.Api.PORT;
import static com.via.letmein.persistence.api.Api.QUERY_DELIMITER;
import static com.via.letmein.persistence.api.Api.SESSION_ID;

/**
 * Adapter for {@See AdministrationFragment#membersRecyclerView}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<HouseholdMember> data;
    private final Context context;
    private final OnItemClickListener onItemClickListener;

    MemberAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
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
        holder.name.setText(householdMember.getName());
        String url = HTTP +
                Session.getInstance(context).getIpAddress() +
                ADDRESS_PORT_DELIMITER +
                PORT +
                householdMember.getProfilePhoto() +
                QUERY_DELIMITER +
                SESSION_ID +
                PARAMETER_DELIMITER +
                Session.getInstance(context).getSessionId();
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.profile_icon_placeholder_background)
                .into(holder.portrait);
       /* Picasso.get()
                .load("https://image.shutterstock.com/image-vector/woman-avatar-isolated-on-white-260nw-1472212124.jpg")
                .placeholder(R.drawable.profile_icon_placeholder_background)
                .into(holder.portrait);*/

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
        final ImageView portrait;
        final TextView name;

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
