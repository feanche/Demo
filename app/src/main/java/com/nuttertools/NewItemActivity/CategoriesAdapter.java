package com.nuttertools.NewItemActivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nuttertools.R;
import com.nuttertools.fragments.Category.Category;

import java.util.ArrayList;

/**
 * Created by Alexander on 07.02.2018.
 */

public class CategoriesAdapter extends ArrayAdapter<Category>{
    private Activity context;
    ArrayList<Category> data;

    public CategoriesAdapter(Activity context, int resource, ArrayList<Category> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_layout, parent, false);
        }

        Category item = data.get(position);

        if (item != null) {
            TextView myCountry = row.findViewById(R.id.text_spinner_layout);
            if (myCountry != null)
                myCountry.setText(item.getName());
        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_dropdown_layout, parent, false);
        }

        Category item = data.get(position);

        if (item != null) {
            TextView myCountry = row.findViewById(R.id.text_spinner_dropdown);
            if (myCountry != null)
                myCountry.setText(item.getName());
        }

        return row;
    }

    @Override
    public Category getItem(int position) {
        return data.get(position);
    }
}
