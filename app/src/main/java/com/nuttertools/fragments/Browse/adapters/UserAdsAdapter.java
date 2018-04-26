package com.nuttertools.fragments.Browse.adapters;

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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexander on 11.01.2018.
 */

public class UserAdsAdapter extends RecyclerView.Adapter<UserAdsAdapter.UserViewHolder> {

    Context context;
    private List<UserAdsModel> list;

    public UserAdsAdapter(Context context, List<UserAdsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_browse_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserAdsModel user = list.get(position);
        holder.tvTitle.setText(user.getTitle());
        holder.tvDesc.setText(user.getDescription());
        //holder.tvPrice.setText(String.valueOf(user.getPrice()));

        /*SimpleDateFormat sf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        String date = sf.format(new Date(user.getPublicTime()));
        holder.tvDate.setText("Время публикации: \n" +  date);*/

        DecimalFormat dfnd = new DecimalFormat("#,###.00");
        holder.tvPrice.setText((dfnd.format(user.getPrice())).concat(" \u20BD/").concat(user.getPriceType()));


        if(user.getPhotoUrl().size() != 0)
            GlideApp.with(context)
                    .load(user.getPhotoUrl().get(0))
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder  {
        TextView tvTitle;
        TextView tvDesc;
        TextView tvDate;
        TextView tvPrice;
        ImageView imageView;
        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}