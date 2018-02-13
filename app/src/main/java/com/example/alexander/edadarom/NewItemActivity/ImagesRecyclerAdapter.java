package com.example.alexander.edadarom.NewItemActivity;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.alexander.edadarom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by GabdrakhmanovII on 03.11.2017.
 */

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<UploadImage> mList;
    private Context context;

    BtnClickListener listener;
    private DotsClickListener dotsClickListener;

    interface DotsClickListener {
        void onClick(int position);
    }

    public void setClickListener(DotsClickListener dotsClickListener) {
        this.dotsClickListener = dotsClickListener;
    }

    public interface BtnClickListener {
        void ivAddClick();

        void ivDelClick(int position);
    }


    public ImagesRecyclerAdapter(Context context, ArrayList<UploadImage> itemList, BtnClickListener listener) {
        this.context = context;
        this.mList = itemList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_adphoto, parent, false);
        return new SecondViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position == mList.size() - 1) {
            ((SecondViewHolder) holder).ivAdd.setVisibility(View.VISIBLE);
            ((SecondViewHolder) holder).ivDel.setVisibility(View.GONE);
            ((SecondViewHolder) holder).progressBar.setVisibility(View.GONE);
            ((SecondViewHolder) holder).imageView.setVisibility(View.INVISIBLE);

            ((SecondViewHolder) holder).ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ivAddClick();
                }
            });
        } else {
            ((SecondViewHolder) holder).ivAdd.setVisibility(View.GONE);
            ((SecondViewHolder) holder).ivDel.setVisibility(View.VISIBLE);
            ((SecondViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
            ((SecondViewHolder) holder).imageView.setVisibility(View.VISIBLE);

            if(!mList.get(position).isLoaded()) {
                ((SecondViewHolder) holder).imageView.setAlpha(0.5f);
                ((SecondViewHolder) holder).progressBar.setProgress(mList.get(position).getProgress());
            } else {
                ((SecondViewHolder) holder).imageView.setAlpha(1f);
                ((SecondViewHolder) holder).progressBar.setVisibility(View.INVISIBLE);
            }


            Picasso.with(context).load(mList.get(position).getUri()).fit().centerCrop().into(((SecondViewHolder) holder).imageView);

            ((SecondViewHolder) holder).ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ivDelClick(position);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    public void add(Uri uri) {
        mList.add(mList.size() - 1, new UploadImage(uri, false));
        notifyDataSetChanged();
    }

    public void setIsLoaded(int position, boolean b) {
        mList.get(position).setLoaded(b);
        notifyDataSetChanged();
    }

    public void setProgress(int position, int progress) {
        mList.get(position).setProgress(progress);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static class SecondViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView ivAdd;
        public ImageView ivDel;
        public ProgressBar progressBar;

        public SecondViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivPhoto);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();

            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
            ivDel = (ImageView) itemView.findViewById(R.id.ivDel);
            progressBar = (ProgressBar) itemView.findViewById(R.id.photoAddProgressBar);
            progressBar.setProgress(0);
            progressBar.setMax(100);
        }
    }
}
