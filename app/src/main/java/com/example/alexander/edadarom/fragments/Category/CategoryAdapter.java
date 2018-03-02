package com.example.alexander.edadarom.fragments.Category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.utils.GlideApp;
import com.example.alexander.edadarom.utils.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alexander on 11.01.2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.UserViewHolder> {

    Context context;
    private List<Category> list;

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_category_recycler_item, parent,false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Category category = list.get(position);
        holder.tvTitle.setText(category.getName());
        holder.tvDesc.setText(category.getDescription());

        /*SimpleDateFormat sf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        String date = sf.format(new Date(user.getPublicTime()));
        holder.tvDate.setText("Время публикации: \n" +  date);*/

        if(category.getImgUrl()!=null)
        GlideApp.with(context)
                .load(category.getImgUrl())
                .fitCenter()
                .into(holder.imageView);
        //TODO .fit >>> .fitCenter()
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder  {
        TextView tvTitle;
        TextView tvDesc;
        TextView tvDate;
        SquareImageView imageView;
        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
