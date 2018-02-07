package com.example.alexander.edadarom.Reservations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.example.alexander.edadarom.utils.ItemClickSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;


public class ReservationsActivity extends AppCompatActivity {

    public static final String TAG = "ReservationActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<UserAdsModel> ar = new ArrayList<>();
    private ReservationAdapter adapter;
    private RecyclerView recyclerView;
    private MaterialDialog dialog;

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
        getSupportActionBar().setTitle("Мои бронирования");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initRecyclerView();
        getDate();
    }

    private void initRecyclerView() {
        adapter = new ReservationAdapter(getApplicationContext(), ar);
        recyclerView = findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ReservationAdapter.DotsClickListener() {
            @Override
            public void onClick(int position) {
                createListDialog(position);
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

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
            db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
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

    public void cancelReservation(int position) {
        dialog = createDialog();
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserAdsModel ads = ar.get(position);

        if (firebaseUser != null) {
            // Get a new write batch
            WriteBatch batch = db.batch();

            // Update the Ads
            DocumentReference adsRef = db.collection(FirebaseConst.ADS).document(ads.getId());
            DocumentReference myReservationRef = db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.MY_RESERVATIONS).document(ads.getId());


            //Добавляем её к модели пользователя
            ads.setReservationInfo(null);
            //добавляем информацию о статусе бронирование
            ads.setReserved(false);

            batch.set(adsRef, ads);
            batch.delete(myReservationRef);

            // Commit the batch
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ar.remove(position);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "batch success");
                   dialog.dismiss();
                }
            });
        }

    }

    public MaterialDialog createDialog() {
        return new MaterialDialog.Builder(this)
                .content("Пожалуйста, подождите!")
                .progress(true, 0)
                .show();
    }

    public MaterialDialog createListDialog(int position) {
       return new MaterialDialog.Builder(this)
               .items(R.array.dialog_reservation_activity)
               .itemsCallback(new MaterialDialog.ListCallback() {
                   @Override
                   public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                       switch (which) {
                           case 0:
                               cancelReservation(position);
                               break;
                       }
                   }
               })
               .show();
    }
}
