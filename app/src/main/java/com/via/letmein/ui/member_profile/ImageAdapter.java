package com.via.letmein.ui.member_profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the pictures of the HouseholdMember.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<ImageContainer> data;
    private final OnItemClickListener onItemClickListener;

    public ImageAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        data = new ArrayList<>();
    }

    public List<ImageContainer> getData() {
        return data;
    }

    public void setData(List<ImageContainer> data) {
        this.data = data;
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
        ImageContainer dummy = data.get(position);
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
