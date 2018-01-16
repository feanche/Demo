package com.example.alexander.edadarom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;

import java.util.List;

/**
 * Created by Alexander on 11.01.2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserAdsModel> list;

    public UserAdapter(List<UserAdsModel> list) {
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_browse_cards,parent,false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserAdsModel user = list.get(position);
        holder.title.setText(user.title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder  {
        EditText title;
        public UserViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.editText);
        }
    }
}
