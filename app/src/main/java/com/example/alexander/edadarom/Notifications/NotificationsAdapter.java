package com.example.alexander.edadarom.Notifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Alexander on 11.01.2018.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.UserViewHolder> {

    Context context;
    private List<Notification> list;

    public NotificationsAdapter(Context context, List<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Notification notif = list.get(position);
        holder.tvTitle.setText(notif.getTitle());
        holder.tvDesc.setText(notif.getBody());
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        String date = sf.format(notif.getTimestamp());
        holder.tvTimestamp.setText(date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder  {
        TextView tvTitle;
        TextView tvDesc;
        TextView tvTimestamp;
        ImageView imageView;
        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            imageView = itemView.findViewById(R.id.ivToolbar);
        }
    }
}
