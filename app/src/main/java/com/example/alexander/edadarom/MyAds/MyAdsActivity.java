package com.example.alexander.edadarom.MyAds;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.Reservations.ReservationAdapter;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.example.alexander.edadarom.utils.ItemClickSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyAdsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<UserAdsModel> ar = new ArrayList<>();
    private MyAdsAdapter adapter;
    private RecyclerView recyclerView;

    //Toolbar back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_colors));

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Мои объявления");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initRecyclerView();
        getDate();
    }

    private void initRecyclerView() {

        adapter = new MyAdsAdapter(getApplicationContext(), ar);
        recyclerView = findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(MyAdsActivity.this, MyAdsFullActivity.class);
                intent.putExtra("id", ar.get(position).getId());
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    getDate();
                }
        );

    }

    private void getDate() {
        ar.clear();
        swipeRefreshLayout.setRefreshing(true);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_ADS)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().size() == 0) {
                                    //view.showToast("Нет данных");
                                    swipeRefreshLayout.setRefreshing(false);

                                    return;
                                }

                                for (DocumentSnapshot document : task.getResult()) {
                                    UserAdsModel userAdsModel = document.toObject(UserAdsModel.class);
                                    ar.add(userAdsModel);
                                }
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getApplicationContext(), "Ошибка " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
