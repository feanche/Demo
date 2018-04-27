package com.nuttertools.MyAds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nuttertools.R;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.GlideApp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ilnaz on 30.01.2018.
 */

public class MyAdsAdapter extends RecyclerView.Adapter<MyAdsAdapter.UserViewHolder> {


    interface DotsClickListener {
        void onClick(int position);
    }

    private DotsClickListener dotsClickListener;

    Context context;
    private List<UserAdsModel> list;

    public MyAdsAdapter(Context context, List<UserAdsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ads_recycler_item,parent,false));
    }

    public void setDotsClickListener(DotsClickListener dotsClickListener) {
        this.dotsClickListener = dotsClickListener;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserAdsModel user = list.get(position);
        holder.tvTitle.setText(user.getTitle());
        holder.tvDesc.setText(user.getDescription());

        DecimalFormat dfnd = new DecimalFormat("#,###.00");
        holder.tvPrice.setText((dfnd.format(user.getPrice())).concat(" ").concat(user.getPriceType()));

        SimpleDateFormat sf = new SimpleDateFormat("d MMMM HH:mm", new Locale("ru","RU"));
        String date = sf.format(user.getTimestamp());
        holder.tvTimestamp.setText("Дата публикации: ".concat(date));

        GlideApp.with(context)
                .load(user.getPhotoUrl().get(0))
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(holder.imageView);

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
        TextView tvPrice;
        ImageView imageView;
        ImageView ivDots;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imageView = itemView.findViewById(R.id.imageView);
            ivDots = itemView.findViewById(R.id.ivDots);
        }
    }
}
