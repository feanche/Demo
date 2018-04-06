package com.nuttertools.Notifications;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nuttertools.FullInfo.FullInfoActivity;
import com.nuttertools.R;
import com.nuttertools.fragments.Browse.adapters.*;
import com.nuttertools.models.ReservationQuery;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.ItemClickSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class NotificationsActivity extends AppCompatActivity {

    public static final String TAG = "NotificationActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Notification> arNotificatons = new ArrayList<>();
    private NotificationsAdapter adapter;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.activity_notifications_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initRecyclerView();
        getDate();
    }

    private void initRecyclerView() {

        adapter = new NotificationsAdapter(getApplicationContext(), arNotificatons);
        recyclerView = findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                WriteBatch batch = db.batch();
                // Set the value of reservationsQuery
                DocumentReference reservationRef = db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.NOTIFICATIONS).document(arNotificatons.get(viewHolder.getAdapterPosition()).getId());
                batch.delete(reservationRef);
                // Commit the batch
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "batch success");
                    }
                });
                arNotificatons.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
            getDate();
                }
        );

    }

    private void getDate() {
        arNotificatons.clear();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseConst.USERS).document(firebaseUser.getUid()).collection(FirebaseConst.NOTIFICATIONS)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().size() == 0) {
                                    //view.showToast("Нет данных");

                                    return;
                                }

                                for (DocumentSnapshot document : task.getResult()) {
                                    Notification notification = document.toObject(Notification.class);
                                    notification.setId(document.getId());
                                    arNotificatons.add(notification);
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