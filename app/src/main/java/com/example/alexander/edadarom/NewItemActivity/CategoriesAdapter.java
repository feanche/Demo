package com.example.alexander.edadarom.NewItemActivity;

import android.app.Activity;
import android.support.annotation.NonNull;
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

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_layout, parent, false);
        }

        Categories item = data.get(position);

        if (item != null) { // парсим данные с каждого объекта
            TextView myCountry = (TextView) row.findViewById(R.id.countryName);
            if (myCountry != null)
                myCountry.setText(item.getName());
        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) { // этот код выполняется, когда вы нажимаете на спиннер
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_dropdown_layout, parent, false);
        }

        Categories item = data.get(position);

        if (item != null) { // парсим данные с каждого объекта
            TextView myCountry = (TextView) row.findViewById(R.id.countryName);
            if (myCountry != null)
                myCountry.setText(item.getName());
        }

        return row;
    }
}
