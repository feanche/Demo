package com.nuttertools.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuttertools.R;

public class EmptyFragment extends Fragment {

    private View view;

    public static EmptyFragment instance() {
        return new EmptyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.empty_fragment, container, false);
        return view;

    }

}