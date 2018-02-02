package com.example.alexander.edadarom.Reservations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.Notifications.Notification;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.fragments.Browse.adapters.UserAdsAdapter;
import com.example.alexander.edadarom.models.UserAdsModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ilnaz on 30.01.2018.
 */

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.UserViewHolder> {

    interface DotsClickListener {
        void onClick(int position);
    }

    Context context;
    private List<UserAdsModel> list;
    private DotsClickListener dotsClickListener;

    public ReservationAdapter(Context context, List<UserAdsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_recycler_item,parent,false));
    }

    public void setClickListener(DotsClickListener dotsClickListener) {
        this.dotsClickListener = dotsClickListener;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserAdsModel ads = list.get(position);
        holder.tvTitle.setText(ads.getTitle());
        holder.tvDesc.setText(ads.getDescription());
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        String date = sf.format(new Date(ads.getReservationInfo().getTimestamp()));
        holder.tvTimestamp.setText(date);

        holder.ivDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotsClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder  {
        TextView tvTitle;
        TextView tvDesc;
        TextView tvTimestamp;
        ImageView ivDots;
        ImageView imageView;
        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivDots = itemView.findViewById(R.id.ivDots);
            imageView = itemView.findViewById(R.id.ivToolbar);
        }
    }
}
