package com.nuttertools.MyAds;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nuttertools.NewItemActivity.AddNewItemActivity;
import com.nuttertools.R;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.CreateDialog;
import com.nuttertools.utils.EmptyFragment;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.ItemClickSupport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class MyAdsActivity extends AppCompatActivity {

    public static final String TAG = "MyAdsActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<UserAdsModel> ar = new ArrayList<>();
    private MyAdsAdapter adapter;
    private RecyclerView recyclerView;
    private MaterialDialog dialog;
    private FloatingActionButton fab;
    public static final int NEW_ITEM = 1;
    private ConstraintLayout container_empty;

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

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.activity_my_ads_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initRecyclerView();
        getDate();
        initButtons();
    }

    private void initButtons() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddNewItemActivity.class);
            startActivityForResult(intent, NEW_ITEM);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initRecyclerView() {

        adapter = new MyAdsAdapter(getApplicationContext(), ar);
        recyclerView = findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(MyAdsActivity.this, MyAdsFullActivity.class);
            intent.putExtra("id", ar.get(position).getId());
            startActivity(intent);
        });

        adapter.setDotsClickListener(position -> createListDialog(position));

        swipeRefreshLayout.setOnRefreshListener(() -> getDate());

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
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            for (DocumentSnapshot document : task.getResult()) {
                                UserAdsModel userAdsModel = document.toObject(UserAdsModel.class);
                                userAdsModel.setId(document.getId());
                                ar.add(userAdsModel);
                            }
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Ошибка " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener(queryDocumentSnapshots -> emptyCheck());
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void deleteAds(int position) {
        dialog = CreateDialog.createPleaseWaitDialog(MyAdsActivity.this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserAdsModel ads = ar.get(position);

        if (firebaseUser != null) {
            WriteBatch batch = db.batch();
            DocumentReference myReservationRef = db
                    .collection(FirebaseConst.USERS)
                    .document(firebaseUser.getUid())
                    .collection(FirebaseConst.MY_ADS)
                    .document(ads.getId());
            batch.delete(myReservationRef);

            batch.commit().addOnCompleteListener(task -> {
                ar.remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            });
        }
    }

    public MaterialDialog createListDialog(int position) {
        return new MaterialDialog.Builder(this)
                .items(R.array.dialog_my_ads_activity)
                .itemsCallback((dialog, view, which, text) -> {
                    switch (which) {
                        case 0:
                            deleteAds(position);
                            break;
                    }
                })
                .show();
    }

    private void emptyCheck() {
        container_empty = findViewById(R.id.container_empty_notifications);
        if (adapter.getItemCount() == 0) {
            EmptyFragment emptyFragment = EmptyFragment.instance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_empty_notifications, emptyFragment)
                    .commit();
            container_empty.setVisibility(View.VISIBLE);
        } else {
            container_empty.setVisibility(View.GONE);
        }
    }

}