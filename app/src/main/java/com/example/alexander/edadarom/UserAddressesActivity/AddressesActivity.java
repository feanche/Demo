package com.example.alexander.edadarom.UserAddressesActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.example.alexander.edadarom.FullInfo.FragmentReservationOptions;
import com.example.alexander.edadarom.Notifications.Notification;
import com.example.alexander.edadarom.Notifications.NotificationsAdapter;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.ItemClickSupport;

import java.util.ArrayList;

/**
 * Created by Alexander on 29.01.2018.
 */

public class AddressesActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    String region, city, address, index;

    ConstraintLayout topViewAddress;

    private ArrayList<Users> arAddress = new ArrayList<>();
    private AddressesRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        topViewAddress = (ConstraintLayout) findViewById(R.id.topViewAddress);

        btnClickListeners();
    }

    private void btnClickListeners() {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressFragment();
            }
        });
    }

    public void showAddAddressFragment() {
        FragmentAddAddress fragment = new FragmentAddAddress();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.topViewAddress, fragment)
                .addToBackStack("reservation")
                .commit();
        setTheme(R.style.AppTheme);
        setStatusBarTranslucent(false);
        floatingActionButton.setVisibility(View.GONE);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void initRecyclerView() {

        adapter = new AddressesRecyclerAdapter(getApplicationContext(), arAddress);
        recyclerView = findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

    }

}