package com.via.letmein.ui.member_profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;

import java.util.ArrayList;
import java.util.List;

import static com.via.letmein.persistence.api.Api.ADDRESS_PORT_DELIMITER;
import static com.via.letmein.persistence.api.Api.HTTP;
import static com.via.letmein.persistence.api.Api.PARAMETER_DELIMITER;
import static com.via.letmein.persistence.api.Api.PORT;
import static com.via.letmein.persistence.api.Api.QUERY_DELIMITER;
import static com.via.letmein.persistence.api.Api.SESSION_ID;

/**
 * Adapter for the pictures of the HouseholdMember.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<ImageContainer> data;
    private final OnItemClickListener onItemClickListener;
    private final Context context;

    public ImageAdapter(OnItemClickListener onItemClickListener, Context context) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        data = new ArrayList<>();
    }

    public List<ImageContainer> getData() {
        return data;
    }

    public void setData(List<String> paths) {

        data = new ArrayList<>();
        for (String imagePath : paths)
            data.add(new ImageContainer(imagePath));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        ImageContainer imageContainer = data.get(position);

        String url = HTTP +
                Session.getInstance(context).getIpAddress() +
                ADDRESS_PORT_DELIMITER +
                PORT +
                imageContainer.getPath() +
                QUERY_DELIMITER +
                SESSION_ID +
                PARAMETER_DELIMITER +
                Session.getInstance(context).getSessionId();

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.profile_icon_placeholder_background)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView image;
        final ImageView checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            checkbox = itemView.findViewById(R.id.checkbox);
            checkbox.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ImageContainer dummy = data.get(getAdapterPosition());
            dummy.toggleSelected();

            if (dummy.isSelected()) {
                image.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.selectImageTint));
                checkbox.setVisibility(View.VISIBLE);
            } else {
                image.clearColorFilter();
                checkbox.setVisibility(View.INVISIBLE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }
}
