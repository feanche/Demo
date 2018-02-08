package com.example.alexander.edadarom.UserAddressesActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alexander.edadarom.MapsActivity;
import com.example.alexander.edadarom.NewItemActivity.AddNewItemActivity;
import com.example.alexander.edadarom.models.Address;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.example.alexander.edadarom.utils.ItemClickSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.alexander.edadarom.R;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

/**
 * Created by Alexander on 29.01.2018.
 */

public class AddressesActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    String uId;
    String locality, comment;
    float locationLat, locationLon;

    FirebaseFirestore db;

    ConstraintLayout topViewAddress;
    ImageView ivClose, delete_button;

    private AddressesRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Address> arAddress = new ArrayList<>();

    public static final int GET_MAP = 1000;
    private String addressId;

    public static final String EXTRA_LAT = "com.nutter.tools.EXTRA_LAT";
    public static final String EXTRA_LON = "com.nutter.tools.EXTRA_LON";
    public static final String EXTRA_COMMENT = "com.nutter.tools.COMMENT";
    public static final String EXTRA_LOCALITY = "com.nutter.tools.LOCALITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        topViewAddress = findViewById(R.id.topViewAddress);
        floatingActionButton = findViewById(R.id.fab);
        topViewAddress = findViewById(R.id.topViewAddress);
        ivClose = findViewById(R.id.iv_close);
        //delete_button = findViewById(R.id.delete_button);
        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = firebaseUser.getUid();
        btnClickListeners();
        initRecyclerView();
    }

    private void btnClickListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, GET_MAP);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteAddress(int position) {
        Address address = arAddress.get(position);
        WriteBatch batch = db.batch();
        DocumentReference myAddressRef = db.collection(FirebaseConst.USERS).document(uId).collection(FirebaseConst.ADDRESS).document(addressId);
        batch.delete(myAddressRef);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                arAddress.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getData() {
        Address address = new Address();
        address.setCommentToAddress(comment);
        address.setLocationLat(locationLat);
        address.setLocationLon(locationLon);
        address.setLocality(locality);
        sendToFirestore(address);
    }

    public void sendToFirestore(Address address) {
        db.collection("users").document(uId).collection("address")
                .add(address)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("mylogs", "Success");
                        initRecyclerView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("mylogs", "Error");
                    }
                });
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uId).collection("address")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                return;
                            }
                            for (DocumentSnapshot document : task.getResult()) {
                                Address address = document.toObject(Address.class);
                                addressId = document.getId();
                                arAddress.add(address);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        Intent intent = getIntent();

        if(getIntent().getExtras()!=null){
            String message = intent.getStringExtra(AddNewItemActivity.EXTRA_MESSAGE);
            if(message.equals("shit")){


                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent replyIntent = new Intent();
                        replyIntent.putExtra(EXTRA_LAT, arAddress.get(position).getLocationLat());
                        replyIntent.putExtra(EXTRA_LON, arAddress.get(position).getLocationLon());
                        replyIntent.putExtra(EXTRA_COMMENT, arAddress.get(position).getCommentToAddress());
                        replyIntent.putExtra(EXTRA_LOCALITY, arAddress.get(position).getLocality());
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                });

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                locationLat = data.getExtras().getFloat(MapsActivity.LOCATION_LAT);
                locationLon = data.getExtras().getFloat(MapsActivity.LOCATION_LON);
                getData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}