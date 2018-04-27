package com.nuttertools.UserAddressesActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuttertools.R;
import com.nuttertools.models.Address;

import java.util.ArrayList;

/**
 * Created by Alexander on 29.01.2018.
 */

public class AddressesRecyclerAdapter extends RecyclerView.Adapter<AddressesRecyclerAdapter.UserAddressViewHolder> {

    Context context;
    private ArrayList<Address> arrayList;
    private DotsClickListener dotsClickListener;

    interface DotsClickListener {
        void onClick(int position);
    }

    public AddressesRecyclerAdapter(Context context, ArrayList<Address> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public UserAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserAddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_addresses_item,parent,false));
    }

    public void setClickListener(DotsClickListener dotsClickListener) {
        this.dotsClickListener = dotsClickListener;
    }

    @Override
    public void onBindViewHolder(UserAddressViewHolder holder, int position) {
        Address address = arrayList.get(position);
        holder.tvTitle.setText(address.getCommentToAddress());
        holder.tvAddress.setText(address.getLocality());
        if(address.getDefaultAddress()){
            holder.checkDefault.setVisibility(View.VISIBLE);
        } else {
            holder.checkDefault.setVisibility(View.GONE);
        }
        holder.ivDots.setOnClickListener(v -> dotsClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class UserAddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAddress;
        ImageView ivDots, checkDefault;

        public UserAddressViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivDots = itemView.findViewById(R.id.ivDots);
            checkDefault = itemView.findViewById(R.id.imageView5);
        }
    }
}