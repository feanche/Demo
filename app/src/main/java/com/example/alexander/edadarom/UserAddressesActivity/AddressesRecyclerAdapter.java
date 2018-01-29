package com.example.alexander.edadarom.UserAddressesActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.edadarom.NewItemActivity.UploadImage;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.models.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 29.01.2018.
 */

public class AddressesRecyclerAdapter extends RecyclerView.Adapter<AddressesRecyclerAdapter.UserViewHolder> {

    Context context;
    private List<Users> list;

    public AddressesRecyclerAdapter(Context context, List<Users> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_addresses_item,parent,false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Users user = list.get(position);
        holder.tvTitle.setText(user.getAddress().size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
