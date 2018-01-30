package com.example.alexander.edadarom.UserAddressesActivity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.edadarom.FullInfo.FullInfoActivity;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.fragments.Browse.adapters.UserAdsAdapter;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander on 30.01.2018.
 */

public class FragmentAddAddress extends Fragment {

    private View view;

    CardView btnSave;
    AddressesRecyclerAdapter adapter;
    RecyclerView recyclerView;
    TextInputEditText txtRegion, txtCity, txtAddress, txtIndex;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_address, container, false);
        btnSave = (CardView)view.findViewById(R.id.btnSave);
        txtRegion = (TextInputEditText)view.findViewById(R.id.txtRegion);
        txtCity = (TextInputEditText)view.findViewById(R.id.txtCity);
        txtAddress = (TextInputEditText)view.findViewById(R.id.txtAddress);
        txtIndex = (TextInputEditText)view.findViewById(R.id.txtIndex);
        btnClickListeners();
        return view;
    }

    private void btnClickListeners() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
