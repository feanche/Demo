package com.example.alexander.edadarom.NewItemActivity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alexander.edadarom.models.Categories;
import com.example.alexander.edadarom.R;

import java.util.ArrayList;

/**
 * Created by Alexander on 07.02.2018.
 */

public class CategoriesAdapter extends ArrayAdapter<Categories> {
    private Activity context;
    ArrayList<Categories> data = null;

    public CategoriesAdapter(Activity context, int resource, ArrayList<Categories> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_layout, parent, false);
        }
        Categories item = data.get(position);
        if(item != null) {
            TextView category_name = row.findViewById(R.id.category_name);
            if(category_name != null)
                category_name.setText(item.getName());
        }
        return row;
    }
}
