package com.nuttertools.FullInfo;

/**
 * Created by lAntimat on 03.12.2017.
 */
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nuttertools.R;
import com.nuttertools.utils.GlideApp;

import java.util.ArrayList;

public class AppbarImagesAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public AppbarImagesAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.appbar_image_slide, view, false);
        ImageView myImage = myImageLayout.findViewById(R.id.image);

        GlideApp.with(context)
                .load(images.get(position))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(myImage);
        view.addView(myImageLayout);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
