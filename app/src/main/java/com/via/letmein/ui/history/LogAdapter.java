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
import com.via.letmein.persistence.model.LoggedAction;

import java.util.List;

import static com.via.letmein.persistence.api.Api.ADDRESS_PORT_DELIMITER;
import static com.via.letmein.persistence.api.Api.HTTP;
import static com.via.letmein.persistence.api.Api.PARAMETER_DELIMITER;
import static com.via.letmein.persistence.api.Api.PORT;
import static com.via.letmein.persistence.api.Api.QUERY_DELIMITER;
import static com.via.letmein.persistence.api.Api.SESSION_ID;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private final List<LoggedAction> data;
    private final Context context;

    public LogAdapter(List<LoggedAction> data, Context context) {
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
        LoggedAction loggedAction = data.get(position);

        holder.name.setText(loggedAction.getName());
        holder.action.setText(loggedAction.getInfoPretty());
        holder.time.setText(loggedAction.getHours(LoggedAction.DATE_FORMAT_FULL));


        /*
         //Testing mockup
         *Picasso.get()
            .load("https://image.shutterstock.com/image-vector/woman-avatar-isolated-on-white-260nw-1472212124.jpg")
             .placeholder(R.drawable.profile_icon_placeholder_background)
             .into(holder.image);
         */

        //construct the url path
        //looks something like this: 'http://192.137.81.144:8080/api/user/image/0?session_id=asghaljbgl2'
        String url = HTTP +
                Session.getInstance(context).getIpAddress() +
                ADDRESS_PORT_DELIMITER +
                PORT +
                loggedAction.getProfilePhoto() +
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
