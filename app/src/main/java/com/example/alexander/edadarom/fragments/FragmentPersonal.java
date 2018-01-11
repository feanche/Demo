package com.example.alexander.edadarom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.edadarom.R;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentPersonal extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        return view;
    }
}
