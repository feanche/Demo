package com.nuttertools.fragments.MyReservations.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuttertools.R;
import com.nuttertools.models.ReservationInfo;
import com.nuttertools.models.UserAdsModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.UserViewHolder>  {

    public interface DotsClickListener {
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
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_recycler_item, parent, false));
    }

    public void setClickListener(DotsClickListener dotsClickListener) {
        this.dotsClickListener = dotsClickListener;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserAdsModel ads = list.get(position);
        holder.tvTitle.setText(ads.getTitle());
        holder.tvDesc.setText(ads.getDescription());
        SimpleDateFormat sf = new SimpleDateFormat("d MMMM HH:mm", new Locale("ru", "RU"));
        String date = sf.format(ads.getReservationInfo().getReservationDate());
        String dateEnd = sf.format(ads.getReservationInfo().getReservationDateEnd());
        holder.tvTimestamp.setText("Начало брони " + date + "\nКонец брони " + dateEnd);
        setStatus(holder.tvStatus, ads.getReservationInfo());
        holder.ivDots.setOnClickListener(v -> dotsClickListener.onClick(position));
    }

    private void setStatus(TextView textView, ReservationInfo info) {
        switch (info.getStatus()) {
            case ReservationInfo.STATUS_FREE:

                break;
            case ReservationInfo.STATUS_WAIT_CONFIRM:
                textView.setText(R.string.tv_pending_approval);
                textView.setTextColor(ContextCompat.getColor(context, R.color.yellow_700));
                break;
            case ReservationInfo.STATUS_CONFIRMED:
                textView.setText(R.string.tv_confirmed);
                textView.setTextColor(ContextCompat.getColor(context, R.color.green_700));
                break;
            case ReservationInfo.STATUS_WAIT_RETURN:
                textView.setText(R.string.tv_waiting_for_return);
                textView.setTextColor(ContextCompat.getColor(context, R.color.yellow_700));
                break;
            case ReservationInfo.STATUS_NOT_CONFIRMED:
                textView.setText(R.string.tv_not_verified);
                textView.setTextColor(ContextCompat.getColor(context, R.color.red_700));
                break;
            default:
                textView.setText(R.string.tv_unknown_status);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDesc;
        TextView tvTimestamp;
        TextView tvStatus;
        ImageView ivDots;
        ImageView imageView;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivDots = itemView.findViewById(R.id.ivDots);
            imageView = itemView.findViewById(R.id.ivToolbar);
        }
    }
}