package com.example.alexander.edadarom.fragments.Browse.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.squareup.picasso.Picasso;

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
        if(user.getPhotoUrl().size() != 0)
        Picasso.with(context).load(user.getPhotoUrl().get(0)).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder  {
        TextView tvTitle;
        TextView tvDesc;
        ImageView imageView;
        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            imageView = itemView.findViewById(R.id.ivToolbar);
        }
    }
}
