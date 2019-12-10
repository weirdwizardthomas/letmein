package com.via.letmein.ui.history;

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
import com.via.letmein.persistence.model.Log;

import java.util.List;

import static com.via.letmein.persistence.api.Api.ADDRESS_PORT_DELIMITER;
import static com.via.letmein.persistence.api.Api.HTTP;
import static com.via.letmein.persistence.api.Api.PARAMETER_DELIMITER;
import static com.via.letmein.persistence.api.Api.PORT;
import static com.via.letmein.persistence.api.Api.QUERY_DELIMITER;
import static com.via.letmein.persistence.api.Api.SESSION_ID;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private final List<Log> data;
    private final Context context;

    public LogAdapter(List<Log> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_visit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ViewHolder holder, int position) {
        Log log = data.get(position);

        holder.name.setText(log.getName());
        holder.action.setText(log.getInfoPretty());
        holder.time.setText(log.getHours(Log.DATE_FORMAT_FULL));


        /*
         //Testing mockup
         *Picasso.get()
            .load("https://image.shutterstock.com/image-vector/woman-avatar-isolated-on-white-260nw-1472212124.jpg")
             .placeholder(R.drawable.profile_icon_placeholder_background)
             .into(holder.image);
         */

        //construct the url path
        //looks something like this: 'http://192.137.81.144:8080/api/user/image/0?session_id=asghaljbgl2'
        String url = new StringBuilder()
                .append(HTTP)
                .append(Session.getInstance(context).getIpAddress())
                .append(ADDRESS_PORT_DELIMITER)
                .append(PORT)
                .append(log.getProfilePhoto())
                .append(QUERY_DELIMITER)
                .append(SESSION_ID)
                .append(PARAMETER_DELIMITER)
                .append(Session.getInstance(context).getSessionId())
                .toString();
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.profile_icon_placeholder_background)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView name;
        final TextView action;
        final TextView time;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.portrait);
            name = itemView.findViewById(R.id.name);
            action = itemView.findViewById(R.id.action);
            time = itemView.findViewById(R.id.time);
        }
    }
}
