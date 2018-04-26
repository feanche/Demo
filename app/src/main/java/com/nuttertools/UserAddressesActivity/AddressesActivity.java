package com.nuttertools.UserAddressesActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nuttertools.MapsActivity;
import com.nuttertools.NewItemActivity.AddNewItemActivity;
import com.nuttertools.models.Address;
import com.nuttertools.utils.CreateDialog;
import com.nuttertools.utils.EmptyFragment;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.ItemClickSupport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.nuttertools.R;

import java.util.ArrayList;

/**
 * Created by Alexander on 29.01.2018.
 */

public class AddressesActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    String uId;
    String locality, comment;
    double locationLat, locationLon;

    private MaterialDialog dialog;

    FirebaseFirestore db;

    private AddressesRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Address> arAddress = new ArrayList<>();

    public static final int GET_MAP = 1000;

    public static final String EXTRA_LAT = "com.nutter.tools.EXTRA_LAT";
    public static final String EXTRA_LON = "com.nutter.tools.EXTRA_LON";
    public static final String EXTRA_COMMENT = "com.nutter.tools.COMMENT";
    public static final String EXTRA_LOCALITY = "com.nutter.tools.LOCALITY";
    private ConstraintLayout container_address;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.activity_my_addresses_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        floatingActionButton = findViewById(R.id.fab);
        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = firebaseUser.getUid();
        btnClickListeners();
        initRecyclerView();
    }

    private void btnClickListeners() {
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivityForResult(intent, GET_MAP);
        });
    }

    private void getData() {
        Address address = new Address();
        address.setCommentToAddress(comment);
        address.setLocationLat(locationLat);
        address.setLocationLon(locationLon);
        address.setLocality(locality);
        address.setDefaultAddress(false);
        sendToFirestore(address);
    }

    public void sendToFirestore(Address address) {
        db.collection(FirebaseConst.USERS).document(uId).collection(FirebaseConst.ADDRESS)
                .add(address)
                .addOnSuccessListener(documentReference -> initRecyclerView())
                .addOnFailureListener(e -> {

                });
    }

    public void getDataFromDatabase() {
        db.collection(FirebaseConst.USERS).document(uId).collection(FirebaseConst.ADDRESS)
                .get()
                .addOnCompleteListener(task -> {
                    arAddress.clear();
                    if (task.isSuccessful()) {
                        if (task.getResult().size() == 0) {
                            return;
                        }
                        for (DocumentSnapshot document : task.getResult()) {
                            Address address = document.toObject(Address.class);
                            address.setId(document.getId());
                            arAddress.add(address);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnSuccessListener(queryDocumentSnapshots -> emptyCheck());
    }

    public void initRecyclerView() {
        arAddress = new ArrayList<>();
        adapter = new AddressesRecyclerAdapter(getApplicationContext(), arAddress);
        recyclerView = findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        dialog = CreateDialog.createPleaseWaitDialog(AddressesActivity.this);
        getDataFromDatabase();
        dialog.dismiss();
        Intent intent = getIntent();
        if(getIntent().getExtras()!=null){
            String message = intent.getStringExtra(AddNewItemActivity.EXTRA_MESSAGE);
            if(message.equals("shit")){
                ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_LAT, arAddress.get(position).getLocationLat());
                    replyIntent.putExtra(EXTRA_LON, arAddress.get(position).getLocationLon());
                    replyIntent.putExtra(EXTRA_COMMENT, arAddress.get(position).getCommentToAddress());
                    replyIntent.putExtra(EXTRA_LOCALITY, arAddress.get(position).getLocality());
                    setResult(RESULT_OK, replyIntent);
                    finish();
                });

            }
        }

        adapter.setClickListener(position -> createListDialog(position));

    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_MAP) {
            if (resultCode == RESULT_OK) {
                comment = data.getExtras().getString(MapsActivity.COMMENT);
                locality = data.getExtras().getString(MapsActivity.LOCALITY);
                locationLat = data.getExtras().getDouble(MapsActivity.LOCATION_LAT);
                locationLon = data.getExtras().getDouble(MapsActivity.LOCATION_LON);
                getData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public MaterialDialog createListDialog(int position) {
        return new MaterialDialog.Builder(this)
                .items(R.array.dialog_addresses_activity)
                .itemsCallback((dialog, view, which, text) -> {
                    switch (which) {
                        case 0:
                            deleteAddress(position);
                            break;
                        case 1:
                            markAsDefaultAddress(position);
                            break;
                    }
                })
                .show();
    }

    private void markAsDefaultAddress(int position) {
        dialog = CreateDialog.createPleaseWaitDialog(AddressesActivity.this);
        Address address = arAddress.get(position);
        String addressId = address.getId();
        DocumentReference userRef = db.collection(FirebaseConst.USERS).document(uId);
        userRef.update("defaultAddress",addressId).addOnSuccessListener(aVoid -> {
            Toast.makeText(getApplicationContext(),"Адрес <"+address.getCommentToAddress()+"> выбран по-умолчанию",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(),"Ошибка изменения адреса по-умолчанию",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        for (int i = 0; i < arAddress.size(); i++) {
            Address addressSearch = arAddress.get(i);
            String addressSearchId = addressSearch.getId();
            if (addressSearch.getDefaultAddress()) {
                addressSearch.setDefaultAddress(false);
                userRef.collection(FirebaseConst.ADDRESS).document(addressSearchId).update("defaultAddress",false);
            }
            addressSearch.setDefaultAddress(true);
            address.setDefaultAddress(true);
            userRef.collection(FirebaseConst.ADDRESS).document(addressId).update("defaultAddress",true);
        }
        getDataFromDatabase();
    }

    private void deleteAddress(int position) {
        dialog = CreateDialog.createPleaseWaitDialog(AddressesActivity.this);
        Address address = arAddress.get(position);
        DocumentReference myAddressRef = db.collection(FirebaseConst.USERS).document(uId).collection(FirebaseConst.ADDRESS).document(address.getId());
        myAddressRef
                .delete()
                .addOnSuccessListener(aVoid -> {
                    arAddress.remove(position);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    emptyCheck();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(),"Ошибка удаления адреса",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
    }

    private void emptyCheck() {
        container_address = findViewById(R.id.container_address);
        if (adapter.getItemCount() == 0) {
            EmptyFragment emptyFragment = EmptyFragment.instance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_address, emptyFragment)
                    .commit();
            container_address.setVisibility(View.VISIBLE);
        } else {
            container_address.setVisibility(View.GONE);
        }
    }

}