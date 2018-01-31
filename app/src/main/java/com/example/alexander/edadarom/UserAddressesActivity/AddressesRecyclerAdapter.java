package com.example.alexander.edadarom.UserAddressesActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.Address;
import com.example.alexander.edadarom.models.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 29.01.2018.
 */

public class AddressesRecyclerAdapter extends RecyclerView.Adapter<AddressesRecyclerAdapter.UserAddressViewHolder> {

    Context context;
    private ArrayList<Address> arrayList;

    public AddressesRecyclerAdapter(Context context, ArrayList<Address> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public UserAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserAddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_addresses_item,parent,false));
    }

    @Override
    public void onBindViewHolder(UserAddressViewHolder holder, int position) {

        //holder.tvTitle.setText(user.getAddress().size());

        Address address = arrayList.get(position);
        holder.tvTitle.setText(address.getTitle());
        holder.tvAddress.setText(address.getAddress()
                +", "
                +address.getCity()
                +", "
                +address.getRegion()
                +", "
                +address.getIndex());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class UserAddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAddress;

        public UserAddressViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }
}
