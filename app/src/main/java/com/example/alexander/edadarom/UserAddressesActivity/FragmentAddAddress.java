package com.example.alexander.edadarom.UserAddressesActivity;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.Address;

/**
 * Created by Alexander on 30.01.2018.
 */

public class FragmentAddAddress extends Fragment {

    private View view;

    CardView btnSave;
    TextInputEditText txtRegion, txtCity, txtAddress, txtIndex, txtTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_address, container, false);
        btnSave = (CardView)view.findViewById(R.id.btnSave);
        txtRegion = (TextInputEditText)view.findViewById(R.id.txtRegion);
        txtCity = (TextInputEditText)view.findViewById(R.id.txtCity);
        txtAddress = (TextInputEditText)view.findViewById(R.id.txtAddress);
        txtIndex = (TextInputEditText)view.findViewById(R.id.txtIndex);
        txtTitle = (TextInputEditText)view.findViewById(R.id.txtTitle);
        btnClickListeners();
        return view;
    }

    private void btnClickListeners() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address address = new Address();
                address.setIndex(txtIndex.getText().toString());
                address.setCity(txtCity.getText().toString());
                address.setAddress(txtAddress.getText().toString());
                address.setRegion(txtRegion.getText().toString());
                address.setTitle(txtTitle.getText().toString());

                ((AddressesActivity)getActivity()).sendToFirestore(address);
                ((AddressesActivity)getActivity()).floatingActionButton.setVisibility(View.VISIBLE);
                ((AddressesActivity)getActivity()).initRecyclerView();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                if (manager.getBackStackEntryCount() != 0) {
                    manager.popBackStack();
                }
            }
        });
    }







}
